package com.nova.shopping.pay.payment.open;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nova.shopping.pay.web.dto.HuaweiPayParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 华为支付工具类
 * @author: wzh
 * @date: 2023/3/2 10:22
 */
@Slf4j(topic = "HuaweiPayment")
@Component
public class HuaweiPayment {

    public static final String GRANT_TYPE = "client_credentials";

    private static final String TOKEN_URL = "https://oauth-login.cloud.huawei.com/oauth2/v3/token";

    public static final String SITE_URL = "https://orders-drcn.iap.cloud.huawei.com.cn";

    public static final String VERIFY_URL = "/applications/purchases/tokens/verify";

    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArkiZK742UZ1o0UHcCY2QFkfHhINs4LBI9A4GcD4aSUzSRp7l/GTuhZwh7npN3h+G7QV1/OuZmTHB8skPZJtqIwzPOsmGQwo8HTK8BH5AHL8rQvVaCZGcXcq8nXzhYq1x3Oys5IGOI6TdkyYYJbUUQr/McqgkPde8i4/UbiH1vbaWEnO75Srf1rQ3BNT0TH5aEcHfQC0n0liktuUeBSLGHiLKyRP+vVgm+n5nbqJR1phBtElotPWCh3T7ZRmWYPfGkg+HZoWZisyX+J4R5kB+ojV63ySKGPwY1BXe3pfRSDxJTLoeYfhq1r14GxWlkNAJKowgCeXBiurdlzmY+5xNAwIDAQAB";

    /**
     * 校验签名信息
     *
     * @param content   结果字符串
     * @param sign      签名字符串
     * @param publicKey IAP公钥
     * @return 是否校验通过
     * @see <a href="https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/verifying-signature-returned-result-0000001050033088">校验签名信息</a>
     * @see <a href="https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/query-payment-info-0000001050166299">IAP公钥</a>
     */
    public boolean doCheck(String content, String sign, String publicKey) {
        return doCheck(content, sign, publicKey, "SHA256WithRSA");
    }

    /**
     * 校验签名信息
     *
     * @param content            结果字符串
     * @param sign               签名字符串
     * @param publicKey          IAP公钥
     * @param signatureAlgorithm 签名算法字段，可从接口返回数据中获取，例如：OwnedPurchasesResult.getSignatureAlgorithm()
     * @return 是否校验通过
     */
    public boolean doCheck(String content, String sign, String publicKey, String signatureAlgorithm) {
        if (sign == null) {
            return false;
        }
        if (publicKey == null) {
            return false;
        }
        // 当signatureAlgorithm为空时使用默认签名算法
        if (signatureAlgorithm == null || signatureAlgorithm.isEmpty()) {
            signatureAlgorithm = "SHA256WithRSA";
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            // 生成"RSA"的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] decodedKey = Base64.decodeBase64(publicKey);
            // 生成公钥
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
            java.security.Signature signature;
            // 根据SHA256WithRSA算法获取签名对象实例
            signature = java.security.Signature.getInstance(signatureAlgorithm);
            // 初始化验证签名的公钥
            signature.initVerify(pubKey);
            // 把原始报文更新到签名对象中
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            // 将sign解码
            byte[] bytes = Base64.decodeBase64(sign);
            // 进行验签
            return signature.verify(bytes);
        } catch (Exception e) {
            log.error("异常信息:", e);
        }
        return false;
    }

    /**
     * 获取token
     *
     * @param param OAuth 2.0客户端ID（凭据）、Connect创建应用之后，系统自动分配的公钥
     * @return
     * @see <a href="https://developer.huawei.com/consumer/cn/doc/development/HMSCore-References/obtain-application-level-at-0000001051066052">获取token</a>
     */
    public Map<String, Object> getAccessToken(HuaweiPayParam param) {
        Map<String, Object> result = new HashMap<>(16);
        try {
            Map<String, Object> params = new HashMap<>(16);
            params.put("client_id", param.getClientId());
            params.put("client_secret", param.getClientSecret());
            params.put("grant_type", GRANT_TYPE);
            String response = HttpUtil.createPost(TOKEN_URL).form(params).execute().body();
            if (StrUtil.isNotBlank(response)) {
                result = JSONUtil.toBean(response, new TypeReference<Map<String, Object>>() {
                }, true);
            }
        } catch (Exception e) {
            log.error("getAccessToken异常:", e);
        } finally {
            log.debug("getAccessToken:{}", result);
        }
        return result;
    }

    /**
     * Order服务验证购买Token
     *
     * @return
     * @see <a href="https://developer.huawei.com/consumer/cn/doc/development/HMSCore-References/api-order-verify-purchase-token-0000001050746113">验证token</a>
     */
    public String verify(HuaweiPayParam param) {
        Map<String, String> headers = new HashMap<>(16);

        String oriString = MessageFormat.format("APPAT:{0}", param.getAccessToken());
        String authorization = MessageFormat.format("Basic {0}", Base64.encodeBase64String(oriString.getBytes(StandardCharsets.UTF_8)));

        headers.put("Authorization", authorization);

        Map<String, String> params = new HashMap<>(16);
        params.put("productId", param.getProductId());
        params.put("purchaseToken", param.getPurchaseToken());
        return HttpUtil.createPost(SITE_URL + VERIFY_URL).addHeaders(headers).formStr(params).execute().body();
    }


    public static void main(String[] args) throws Exception {
        String clientId = "107459075";
        String clientSecret = "f768463eadf6fd27e194e191255ed7ee80f702eb640ee2459093ac400154db66";

        String content = "%7B%22autoRenewing%22%3Afalse%2C%22orderId%22%3A%222022120815023586130a180451.107459075%22%2C%22packageName%22%3A%22com.score.predict%22%2C%22applicationId%22%3A107459075%2C%22applicationIdString%22%3A%22107459075%22%2C%22kind%22%3A0%2C%22productId%22%3A%22test01%22%2C%22productName%22%3A%22%E6%B5%8B%E8%AF%95%E5%95%86%E5%93%81%22%2C%22purchaseTime%22%3A1670482958000%2C%22purchaseTimeMillis%22%3A1670482958000%2C%22purchaseState%22%3A0%2C%22developerPayload%22%3A%2216704829552094113%22%2C%22purchaseToken%22%3A%2200000184f08b182503cfce7f98ec3e6b0c880cf8bdf65e9e1c040c8d95b6bfdec87489e940f2c31dx434e.1.107459075%22%2C%22consumptionState%22%3A0%2C%22confirmed%22%3A0%2C%22purchaseType%22%3A0%2C%22currency%22%3A%22CNY%22%2C%22price%22%3A100%2C%22country%22%3A%22CN%22%2C%22payOrderId%22%3A%22sandbox2022120815023834260932C%22%2C%22payType%22%3A%2271%22%2C%22sdkChannel%22%3A%221%22%7D";

        String decode = URLDecoder.decode(content, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSONUtil.parseObj(decode);
        String purchaseToken = jsonObject.getStr("purchaseToken");
        String productId = jsonObject.getStr("productId");

        HuaweiPayParam.HuaweiPayParamBuilder builder = HuaweiPayParam.builder().clientId(clientId).clientSecret(clientSecret);

        HuaweiPayment payment = new HuaweiPayment();
        Map<String, Object> accessTokenMap = payment.getAccessToken(builder.build());
        String accessToken = MapUtil.getStr(accessTokenMap, "access_token");

        HuaweiPayParam verify = builder.purchaseToken(purchaseToken).productId(productId).accessToken(accessToken).build();

        String result = payment.verify(verify);

        System.out.println("result = " + result);

    }

}
