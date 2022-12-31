package com.nova.pay.payment.open;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.nova.common.utils.http.HttpRequestProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 谷歌支付工具类
 * @author: wzh
 * @date: 2022/10/20 11:32
 */
@Slf4j
@Component
public class GooglePayment {

    public static String PRIVATE_KEYS;

    /**
     * type 1获取 2刷新
     *
     * @param clientId
     * @param clientSecret
     * @param token:type   1 授权code，2：refreshToken
     * @param type
     * @return
     */
    public Map<String, String> getAccessToken(String clientId, String clientSecret, String token, String type) {
        Map<String, String> result = new HashMap<>(16);
        try {
            Map<String, String> headers = MapUtil.builder(new HashMap<String, String>(16))
                    .put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                    .put("Accept", "text/plain;charset=utf-8").build();
            // HTTP 获取access_token
            String url = "https://accounts.google.com/o/oauth2/token";
            Map<String, Object> params = new HashMap<>(16);
            if (StrUtil.equals("1", type)) {
                params.put("grant_type", "authorization_code");
                params.put("code", token);
            } else {
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", token);
            }
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            String accessToken = HttpRequestProxy.sendGet(url, headers, params);
            if (StrUtil.isNotBlank(accessToken)) {
                JSONObject jsonObject = JSONUtil.parseObj(accessToken);
                result.put("accessToken", jsonObject.getStr("access_token"));
                result.put("expiresIn", jsonObject.getStr("expires_in"));
                if (StrUtil.equals("1", type)) {
                    result.put("refreshToken", jsonObject.getStr("refresh_token"));
                }
            }
        } catch (Exception e) {
            log.error("getAccessToken异常:", e);
        } finally {
            log.debug("getAccessTokenMap:{}", JSONUtil.toJsonStr(result));
        }
        return result;
    }

    /**
     * p12验签(方式二)
     *
     * @param accountId
     * @param keyPath
     * @param productId
     * @param packageName
     * @param purchaseToken
     * @param accessToken
     * @return
     */
    public ProductPurchase verify(String accountId, String keyPath, String productId, String packageName, String purchaseToken, String accessToken) {
        try {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
                    new FileInputStream(keyPath),
                    "notasecret", "privateKey", "notasecret");

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(transport).setJsonFactory(JacksonFactory.getDefaultInstance())
                    .setServiceAccountId(accountId)
                    .setServiceAccountScopes(AndroidPublisherScopes.all())
                    .setServiceAccountPrivateKey(privateKey).build();

            AndroidPublisher publisher = new AndroidPublisher.Builder(transport, JacksonFactory.getDefaultInstance(), credential.setAccessToken(accessToken)).build();
            AndroidPublisher.Purchases.Products products = publisher.purchases().products();
            // 参数详细说明: https://developers.google.com/android-publisher/api-ref/purchases/products/get
            AndroidPublisher.Purchases.Products.Get product = products.get(packageName, productId, purchaseToken);
            // 获取订单信息 返回信息说明: https://developers.google.com/android-publisher/api-ref/purchases/products
            // 通过consumptionState, purchaseState可以判断订单的状态
            return product.execute();
        } catch (IOException | GeneralSecurityException e) {
            log.error("verify异常:", e);
        }
        return null;
    }

    /**
     * 方式三
     *
     * @param packageName     应用程序包名
     * @param productId       商品id
     * @param applicationName 应用名称(ScorePredict1x2)
     * @param purchaseToken   谷歌返回的收据
     * @return orderId -----------------谷歌的平台订单号
     * consumptionState-----------------InApp产品的消费状态。可能的值为：0。尚未消耗1.已消耗
     * purchaseState--------------------订单的购买状态。可能的值为：0。已购买1.已取消2.待定
     * obfuscatedExternalProfileId—透传字段，一般传服务端生成的订单id
     * 后面就是处理自己的业务了，大概说下我这次业务，从谷歌得到交易对象purchase后
     * 1.判断订单是否已购买
     * 2.判断订单是否已消耗
     * 3.如果是已购买未消耗的情况下才继续往下走
     * 4.更新数据库订单记录，通过obfuscatedExternalProfileId透传的订单号查询数据库找到该笔订单
     * 5.通知服务发放道具
     *
     *  备注：验签最好拆分一个服务 启动类配置
     *  Properties systemProperties = System.getProperties();
     *  systemProperties.setProperty("http.proxyHost", HOST_NAME);
     *  systemProperties.setProperty("http.proxyPort", PORT);
     *  systemProperties.setProperty("https.proxyHost", HOST_NAME);
     *  systemProperties.setProperty("https.proxyPort", PORT);
     *  systemProperties.setProperty("socksProxyHost", HOST_NAME);
     *  systemProperties.setProperty("socksProxyPort", PORT);
     *  systemProperties.setProperty("http.nonProxyHosts", "localhost");
     *  systemProperties.setProperty("https.nonProxyHosts", "localhost");
     *  //（单位：毫秒）
     *  System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(8000));
     *  System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
     */
    public ProductPurchase verify(String packageName, String applicationName, String productId, String purchaseToken, String keyPath, boolean isProxy) {
        try {
            List<String> scopes = new ArrayList<>();
            scopes.add(AndroidPublisherScopes.ANDROIDPUBLISHER);
            //使用服务帐户Json文件获取Google凭据
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(keyPath)).createScoped(scopes);
            //代理模式
            if (isProxy) {
                //方式一：api设置代理模式（经过测试不好使）

                //NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
                //builder.trustCertificates(GoogleUtils.getCertificateTrustStore());
                //builder.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(HOST_NAME, PORT)));
                //httpTransport = builder.build();

                //方式二：设置系统变量 但是会影响到其他的api 经过测试微信支付掉不起来

                //Properties systemProperties = System.getProperties();
                //systemProperties.setProperty("http.proxyHost", HOST_NAME);
                //systemProperties.setProperty("http.proxyPort", PORT);
                //systemProperties.setProperty("https.proxyHost", HOST_NAME);
                //systemProperties.setProperty("https.proxyPort", PORT);
                //systemProperties.setProperty("socksProxyHost", HOST_NAME);
                //systemProperties.setProperty("socksProxyPort", PORT);
                //systemProperties.setProperty("http.nonProxyHosts", "localhost");
                //systemProperties.setProperty("https.nonProxyHosts", "localhost");
                //（单位：毫秒）
                //System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(8000));
                //System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
            }
            //使用谷歌凭据和收据从谷歌获取购买信息
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            AndroidPublisher publisher = new AndroidPublisher
                    .Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName).build();
            AndroidPublisher.Purchases.Products products = publisher.purchases().products();
            AndroidPublisher.Purchases.Products.Get product = products.get(packageName, productId, purchaseToken);
            return product.execute();
        } catch (IOException | GeneralSecurityException e) {
            log.error("verify异常:", e);
        }
        return null;
    }


    /**
     * 方式1
     *
     * @param content
     * @param sign
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean doCheck(String content, String sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(pubKey);
        signature.update(content.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.decodeBase64(sign));
    }


    public static synchronized String load(String file) {
        if (PRIVATE_KEYS == null) {
            if (!file.startsWith("/")) {
                file = GooglePayment.class.getResource("/").getFile() + File.separator + file;
            }
            File f = new File(file);
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);) {
                StringBuilder buffer = new StringBuilder();
                char[] bf = new char[1024];
                while (true) {
                    int size = reader.read(bf);
                    if (size < 0) {
                        break;
                    }
                    buffer.append(bf, 0, size);
                }
                PRIVATE_KEYS = buffer.toString();
            } catch (IOException e) {
                throw new RuntimeException("read file error ! " + f.getAbsolutePath(), e);
            }
        }
        return PRIVATE_KEYS;
    }


}
