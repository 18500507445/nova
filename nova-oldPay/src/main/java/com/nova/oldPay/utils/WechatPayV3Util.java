package com.nova.oldPay.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.nova.oldPay.config.WechatPayConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 微信支付v3支付
 * @Author: wangzehui
 * @Date: 2022/7/28 19:46
 */
@Component
public class WechatPayV3Util {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayUtil.class);

    /**
     * 商户转账给个人
     *
     * @param wordsId
     * @param openId
     * @param amount  单位分
     * @return
     * @throws IOException
     */
    public String transferToUser(String wordsId, String openId, int amount) {
        String result = "";
        String appId = WechatPayConfig.RED_ENVELOPES_APP_ID;
        String mchId = WechatPayConfig.RED_ENVELOPES_MCH_ID;
        String mchSerialNo = WechatPayConfig.RED_ENVELOPES_MCH_SERIAL_NO;
        String privateKeyPath = WechatPayConfig.RED_ENVELOPES_PRIVATE_CERT_PATH;
        try {
            Map<String, Object> map = new HashMap<>(16);
            //服务商的appId
            map.put("appid", appId);
            map.put("out_batch_no", "batch" + wordsId);
            map.put("batch_name", "红包");
            map.put("batch_remark", "红包兑换");
            //单位分
            map.put("total_amount", amount);
            map.put("total_num", 1);

            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> subMap = new HashMap<>(4);
            subMap.put("out_detail_no", "detail" + wordsId);
            subMap.put("transfer_amount", amount);
            subMap.put("transfer_remark", "红包兑换");
            subMap.put("openid", openId);
            //明细转账金额 >= 2000，收款用户姓名必填
            //X509Certificate x509Certificate = getSaveCertificates("");
            //subMap.put("user_name", RsaCryptoUtil.encryptOAEP("收款用户姓名", x509Certificate));
            list.add(subMap);
            map.put("transfer_detail_list", list);
            String body = JSONUtil.toJsonStr(map);
            logger.info("transferUser请求参数:{}", body);
            //发起转账操作
            result = WechatPayV3Util.postTransBatRequest("https://api.mch.weixin.qq.com/v3/transfer/batches", body, mchSerialNo, mchId, privateKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发起批量转账API 批量转账到零钱
     *
     * @param requestUrl
     * @param requestJson    组合参数
     * @param mchSerialNo    商户证书序列号
     * @param mchId          商户号
     * @param privateKeyPath 商户私钥证书路径
     * @return
     */
    public static String postTransBatRequest(String requestUrl, String requestJson, String mchSerialNo, String mchId, String privateKeyPath) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response;
        HttpEntity entity;
        try {
            //商户私钥证书
            HttpPost httpPost = new HttpPost(requestUrl);
            // NOTE: 建议指定charset=utf-8。低于4.4.6版本的HttpCore，不能正确的设置字符集，可能导致签名错误
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/json");
            //"55E551E614BAA5A3EA38AE03849A76D8C7DA735A");
            httpPost.addHeader("Wechatpay-Serial", mchSerialNo);
            //-------------------------核心认证 start-----------------------------------------------------------------
            String strToken = WechatPayV3Util.getToken("POST", "/v3/transfer/batches", requestJson, mchId, mchSerialNo, privateKeyPath);

            // 添加认证信息
            httpPost.addHeader("Authorization", "WECHATPAY2-SHA256-RSA2048" + " " + strToken);
            //---------------------------核心认证 end---------------------------------------------------------------
            httpPost.setEntity(new StringEntity(requestJson, "UTF-8"));
            //发起转账请求
            response = httpclient.execute(httpPost);
            entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param method       请求方法 post
     * @param canonicalUrl 请求地址
     * @param body         请求参数
     * @param merchantId   这里用的商户号
     * @param certSerialNo 商户证书序列号
     * @param keyPath      商户证书地址
     * @return
     * @throws Exception
     */
    public static String getToken(String method, String canonicalUrl, String body, String merchantId, String certSerialNo, String keyPath) throws Exception {
        String signStr = "";
        //获取32位随机字符串
        String nonceStr = WXPayUtil.generateNonceStr();
        //当前系统运行时间
        long timestamp = System.currentTimeMillis() / 1000;
        if (StringUtils.isEmpty(body)) {
            body = "";
        }
        //签名操作
        String message = buildMessage(method, canonicalUrl, timestamp, nonceStr, body);
        //签名操作
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), keyPath);
        //组装参数
        signStr = "mchid=\"" + merchantId + "\",timestamp=\"" + timestamp + "\",nonce_str=\"" + nonceStr
                + "\",serial_no=\"" + certSerialNo + "\",signature=\"" + signature + "\"";
        return signStr;
    }

    public static String buildMessage(String method, String canonicalUrl, long timestamp, String nonceStr, String body) {
//		String canonicalUrl = url.encodedPath();
//		if (url.encodedQuery() != null) {
//			canonicalUrl += "?" + url.encodedQuery();
//		}
        return method + "\n" + canonicalUrl + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
    }

    public static String sign(byte[] message, String keyPath) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey(keyPath));
        sign.update(message);
        return Base64.encodeBase64String(sign.sign());
    }

    /**
     * 微信支付-前端唤起支付参数-获取商户私钥
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * 根据用户授权码,获取openId
     *
     * @param authCode
     * @return
     */
    public String getOpenId(String authCode, String appId, String appSecret) {
        String openId = "";
        String accessToken;
        String refreshToken;

        String resultMsg = "";
        String resultCode = "";

        String key = "WechatPayV3Util_getOpenId_" + appId + "_" + authCode;
        try {
            //null != redisService.get(key)
            if (true) {
                //openId = redisService.get(key).toString();
            } else {
                logger.info("WechatPayV3Util====>authCode:{}", authCode);
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + authCode + "&grant_type=authorization_code";
                logger.info("WechatPayV3Util====>url:{}", url);
                String result = HttpRequestProxy.doGet(url, "utf-8", "utf-8");
                logger.info("WechatPayV3Util====>result:{}", result);
                JSONObject json = JSONObject.parseObject(result);
                openId = json.getString("openid");
                accessToken = json.getString("access_token");
                refreshToken = json.getString("refresh_token");
                logger.info("WechatPayV3Util:getOpenId-----openId:{},accessToken{},refreshToken{}", openId, accessToken, refreshToken);
                if (StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(accessToken)) {
                    String accessTokenResult = HttpRequestProxy.doGet("https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openId, "utf-8", "utf-8");
                    JSONObject accessJson = JSONObject.parseObject(accessTokenResult);
                    resultCode = accessJson.getString("errcode");
                    resultMsg = accessJson.getString("errmsg");
                    logger.info("WechatPayV3Util:检验授权凭证-----resultCode{},resultMsg{}", resultCode, resultMsg);
                    if (!StringUtils.equals("0", resultCode) && !StringUtils.equals("ok", resultMsg)) {
                        String refreshResult = HttpRequestProxy.doGet("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId + "&grant_type=refresh_token&refresh_token=" + refreshToken, "utf-8", "utf-8");
                        if (StringUtils.isNotEmpty(refreshResult)) {
                            JSONObject refreshJson = JSONObject.parseObject(refreshResult);
                            logger.info("WechatPayV3Util:刷新检验授权凭证" + refreshJson.toJSONString());
                            openId = refreshJson.getString("openid");
                        }
                    }
                }
                if (StringUtils.isNotEmpty(openId)) {
                    //redisService.set(key, openId, 1800L);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openId;
    }

    public static void main(String[] args) throws IOException {
        WechatPayV3Util wechatPayV3Util = new WechatPayV3Util();
        String s = wechatPayV3Util.transferToUser("1213131", "1313131313", 100);
        System.out.println(s);

    }


}
