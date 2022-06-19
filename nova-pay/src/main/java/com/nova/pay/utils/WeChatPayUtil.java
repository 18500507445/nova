package com.nova.pay.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.nova.pay.config.NftWeChatPayConfig;
import com.nova.pay.config.WechatPayConfig;
import com.nova.pay.entity.WeChatPayData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import static com.github.wxpay.sdk.WXPayUtil.generateSignature;


/**
 * @Description: 微信工具类
 * @Author: wangzehui
 * @Date: 2022/6/18 20:46
 */
@Slf4j
public class WeChatPayUtil {

    private static final Logger logger = LoggerFactory.getLogger(WeChatPayUtil.class);

    /**
     * 接入文档：https://pay.weixin.qq.com/wiki/doc/api/index.html
     * 微信下单支付
     * 接口地址h5支付：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_1
     * <p>
     * app：拿到参数然后去拉起支付
     * h5：返回拉起链接
     *
     * @param weChatPayData
     * @throws Exception
     */
    public Map<String, String> weChatPay(WeChatPayData weChatPayData) throws Exception {
        String tradeType = weChatPayData.getTradeType();
        //h5、小程序用一个appId,小程序、微信内用一个
        String appId = ArrayUtils.contains(new String[]{"MWEB", "APP"}, tradeType) ? WechatPayConfig.APP_ID : WechatPayConfig.APP_ID_JSAPI;
        NftWeChatPayConfig config = new NftWeChatPayConfig(appId, WechatPayConfig.MCH_ID, WechatPayConfig.PAY_SECRET, WechatPayConfig.CERT_PATH);

        Map<String, String> hashMap = new HashMap<>(16);
        hashMap.put("appid", appId);
        hashMap.put("mch_id", WechatPayConfig.MCH_ID);
        hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
        hashMap.put("body", weChatPayData.getBody());
        hashMap.put("out_trade_no", weChatPayData.getOutTradeNo());
        hashMap.put("total_fee", weChatPayData.getTotalAmount());
        hashMap.put("spbill_create_ip", weChatPayData.getCreateIp());
        if (StringUtils.equals("JSAPI", tradeType)) {
            //小程序直接传openId,微信内支付自己获取
            if (StringUtils.isNotEmpty(weChatPayData.getOpenId())) {
                hashMap.put("openid", weChatPayData.getOpenId());
            } else {
                hashMap.put("openid", getOpenId(weChatPayData.getAuthCode()));
            }
        }
        hashMap.put("trade_type", tradeType);
        hashMap.put("notify_url", WechatPayConfig.NOTIFY_URL);

        //logger.info("WeChatPayUtil--第一次签名参数:{}", JSONObject.toJSONString(hashMap));

        WXPay wxpay = new WXPay(config);
        Map<String, String> resultMap = wxpay.unifiedOrder(hashMap);

        //返回状态码
        String resultCode = MapUtils.getString(resultMap, "result_code");
        //业务结果
        if (StringUtils.equals(WechatPayConfig.SUCCESS, resultCode)) {
            String timestamp = WechatPayConfig.getCurrentTimestamp();
            String prepayId = MapUtils.getString(resultMap, "prepay_id");

            Map<String, String> param = new HashMap<>(16);
            //todo jsapi、app需进行二次签名,进行验证参数生成的签名和服务器的签名一致才能拉起支付
            if (StringUtils.equals("APP", weChatPayData.getTradeType())) {
                param.put("appid", appId);
                param.put("noncestr", MapUtils.getString(resultMap, "nonce_str"));
                param.put("timestamp", timestamp);
                param.put("partnerid", WechatPayConfig.MCH_ID);
                param.put("prepayid", prepayId);
                param.put("package", "Sign=WXPay");

                String signTwo = generateSignature(param, WechatPayConfig.PAY_SECRET);
                //log.info("weChatPay--app第二次签名参数:{},---signTwo{}", JSONObject.toJSONString(param), signTwo);
                resultMap.put("package", "Sign=WXPay");
                resultMap.put("sign", signTwo);
            } else if (StringUtils.equals("JSAPI", weChatPayData.getTradeType())) {
                param.put("appId", appId);
                param.put("nonceStr", MapUtils.getString(resultMap, "nonce_str"));
                param.put("timeStamp", timestamp);
                param.put("package", "prepay_id=" + prepayId);
                param.put("signType", "MD5");

                String signTwo = generateSignature(param, WechatPayConfig.PAY_SECRET);
                //logger.info("weChatPay--JSAPI第二次签名参数:{},---signTwo{}", JSONObject.toJSONString(param), signTwo);
                resultMap.put("package", "prepay_id=" + prepayId);
                resultMap.put("sign", signTwo);
            }
            resultMap.put("timestamp", timestamp);
        }
        return resultMap;
    }

    /**
     * h5和app 随机商户
     *
     * @param weChatPayData
     * @return
     * @throws Exception
     */
    public Map<String, String> weChatRandom(WeChatPayData weChatPayData) throws Exception {
        String tradeType = weChatPayData.getTradeType();
        //h5、小程序用一个appId,小程序、微信内用一个
        String appId = ArrayUtils.contains(new String[]{"MWEB", "APP"}, tradeType) ? WechatPayConfig.APP_ID : WechatPayConfig.APP_ID_JSAPI;
        String mchId = weChatPayData.getMchId();
        String paySecret = weChatPayData.getPaySecret();
        String certPath = weChatPayData.getCertPath();
        NftWeChatPayConfig config = new NftWeChatPayConfig(appId, mchId, paySecret, certPath);

        Map<String, String> hashMap = new HashMap<>(16);
        hashMap.put("appid", appId);
        hashMap.put("mch_id", mchId);
        hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
        hashMap.put("body", weChatPayData.getBody());
        hashMap.put("out_trade_no", weChatPayData.getOutTradeNo());
        hashMap.put("total_fee", weChatPayData.getTotalAmount());
        hashMap.put("spbill_create_ip", weChatPayData.getCreateIp());
        if (StringUtils.equals("JSAPI", tradeType)) {
            //微信内支付自己获取
            hashMap.put("openid", getOpenId(weChatPayData.getAuthCode()));
        }
        hashMap.put("trade_type", tradeType);
        hashMap.put("notify_url", WechatPayConfig.NOTIFY_URL);

        //logger.info("weChatRandom--第一次签名参数:{}", JSONObject.toJSONString(hashMap));

        WXPay wxpay = new WXPay(config);
        Map<String, String> resultMap = wxpay.unifiedOrder(hashMap);

        //返回状态码
        String resultCode = MapUtils.getString(resultMap, "result_code");
        //业务结果
        if (StringUtils.equals(WechatPayConfig.SUCCESS, resultCode)) {
            String timestamp = WechatPayConfig.getCurrentTimestamp();
            String prepayId = MapUtils.getString(resultMap, "prepay_id");

            Map<String, String> param = new HashMap<>(16);
            //todo jsapi、app需进行二次签名,进行验证参数生成的签名和服务器的签名一致才能拉起支付
            if (StringUtils.equals("APP", weChatPayData.getTradeType())) {
                param.put("appid", appId);
                param.put("noncestr", MapUtils.getString(resultMap, "nonce_str"));
                param.put("timestamp", timestamp);
                param.put("partnerid", mchId);
                param.put("prepayid", prepayId);
                param.put("package", "Sign=WXPay");

                String signTwo = generateSignature(param, paySecret);
                //logger.info("weChatRandom--app第二次签名参数:{},---signTwo{}", JSONObject.toJSONString(param), signTwo);
                resultMap.put("package", "Sign=WXPay");
                resultMap.put("sign", signTwo);
            } else if (StringUtils.equals("JSAPI", weChatPayData.getTradeType())) {
                param.put("appId", appId);
                param.put("nonceStr", MapUtils.getString(resultMap, "nonce_str"));
                param.put("timeStamp", timestamp);
                param.put("package", "prepay_id=" + prepayId);
                param.put("signType", "MD5");

                String signTwo = generateSignature(param, WechatPayConfig.PAY_SECRET);
                //logger.info("weChatRandom--JSAPI第二次签名参数:{},---signTwo{}", JSONObject.toJSONString(param), signTwo);
                resultMap.put("package", "prepay_id=" + prepayId);
                resultMap.put("sign", signTwo);
            }
            resultMap.put("timestamp", timestamp);
        }
        return resultMap;
    }


    /**
     * 退款
     * payType:1h5 2小程序 3app 4微信原生jsapi
     * 备注：目前只有小程序 和原生走这个了
     *
     * @param tradeNo 商户交易订单号
     * @return
     */
    public Map<String, String> refund(Integer payType, String tradeNo, String refundAmount, String refundReason) {
        String appId = ArrayUtils.contains(new int[]{1, 3}, payType) ? WechatPayConfig.APP_ID : WechatPayConfig.APP_ID_JSAPI;
        NftWeChatPayConfig config = new NftWeChatPayConfig(appId, WechatPayConfig.MCH_ID, WechatPayConfig.PAY_SECRET, WechatPayConfig.CERT_PATH);
        try {
            Map<String, String> hashMap = new HashMap<>(16);
            hashMap.put("appid", appId);
            hashMap.put("mch_id", WechatPayConfig.MCH_ID);
            hashMap.put("out_trade_no", tradeNo);
            hashMap.put("out_refund_no", WXPayUtil.generateNonceStr());
            //单位：分
            hashMap.put("total_fee", refundAmount);
            hashMap.put("refund_fee", refundAmount);
            hashMap.put("refund_desc", refundReason);

            WXPay wxpay = new WXPay(config);
            //申请退款
            return wxpay.refund(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机商户退款 h5和app appId是固定这个，mchId随机的
     *
     * @param mchId
     * @param tradeNo
     * @param refundAmount
     * @param refundReason
     * @return
     */
    public Map<String, String> randomRefund(Integer payType, String mchId, String tradeNo, String refundAmount,
                                            String refundReason, String paySecret, String certPath) {
        String appId = ArrayUtils.contains(new int[]{1, 3}, payType) ? WechatPayConfig.APP_ID : WechatPayConfig.APP_ID_JSAPI;
        NftWeChatPayConfig config = new NftWeChatPayConfig(appId, mchId, paySecret, certPath);
        try {
            Map<String, String> hashMap = new HashMap<>(16);
            hashMap.put("appid", appId);
            hashMap.put("mch_id", mchId);
            hashMap.put("out_trade_no", tradeNo);
            hashMap.put("out_refund_no", WXPayUtil.generateNonceStr());
            //单位：分
            hashMap.put("total_fee", refundAmount);
            hashMap.put("refund_fee", refundAmount);
            hashMap.put("refund_desc", refundReason);

            WXPay wxpay = new WXPay(config);
            //申请退款
            return wxpay.refund(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户授权码,获取openId
     *
     * @param authCode
     * @return
     */
    public String getOpenId(String authCode) {
        String openId = "";
        String accessToken;
        String refreshToken;

        String resultMsg = "";
        String resultCode = "";

        String key = "WeChatPayUtil_getOpenId_" + WechatPayConfig.APP_ID_JSAPI + "_" + authCode;
        try {
            //if (null != redisService.get(key)) {
            //    openId = redisService.get(key).toString();
            //} else {
            //    //logger.info("WeChatPayUtil====>authCode:{}", authCode);
            //    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WechatPayConfig.APP_ID_JSAPI + "&secret=" + WechatPayConfig.APP_SECRET_JSAPI + "&code=" + authCode + "&grant_type=authorization_code";
            //    //logger.info("WeChatPayUtil====>url:{}", url);
            //    String result = HttpRequestProxy.doGet(url, "utf-8", "utf-8");
            //    //logger.info("WeChatPayUtil====>result:{}", result);
            //    JSONObject json = JSONObject.parseObject(result);
            //    openId = json.getString("openid");
            //    accessToken = json.getString("access_token");
            //    refreshToken = json.getString("refresh_token");
            //    //logger.info("WeChatPayUtil:getOpenId-----openId:{},accessToken{},refreshToken{}", openId, accessToken, refreshToken);
            //    if (StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(accessToken)) {
            //        String accessTokenResult = HttpRequestProxy.doGet("https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openId, "utf-8", "utf-8");
            //        JSONObject accessJson = JSONObject.parseObject(accessTokenResult);
            //        resultCode = accessJson.getString("errcode");
            //        resultMsg = accessJson.getString("errmsg");
            //        //logger.info("WeChatPayUtil:检验授权凭证-----resultCode{},resultMsg{}", resultCode, resultMsg);
            //        if (!StringUtils.equals("0", resultCode) && !StringUtils.equals("ok", resultMsg)) {
            //            String refreshResult = HttpRequestProxy.doGet("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + WechatPayConfig.APP_ID_JSAPI + "&grant_type=refresh_token&refresh_token=" + refreshToken, "utf-8", "utf-8");
            //            if (StringUtils.isNotEmpty(refreshResult)) {
            //                JSONObject refreshJson = JSONObject.parseObject(refreshResult);
            //                //logger.info("WeChatPayUtil:刷新检验授权凭证" + refreshJson.toJSONString());
            //                openId = refreshJson.getString("openid");
            //            }
            //        }
            //    }
            //    if (StringUtils.isNotEmpty(openId)) {
            //        //redisService.set(key, openId, 1800L);
            //    }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openId;
    }

    /**
     * 查询订单
     *
     * @param payType
     * @param mchId
     * @param orderId
     * @return
     */
    public Map<String, String> queryOrder(Integer payType, String mchId, String orderId, String paySecret, String certPath) {
        String appId = ArrayUtils.contains(new int[]{1, 3}, payType) ? WechatPayConfig.APP_ID : WechatPayConfig.APP_ID_JSAPI;
        if (ArrayUtils.contains(new int[]{2}, payType)) {
            mchId = WechatPayConfig.MCH_ID;
            paySecret = WechatPayConfig.PAY_SECRET;
            certPath = WechatPayConfig.CERT_PATH;
        }
        try {
            NftWeChatPayConfig config = new NftWeChatPayConfig(appId, mchId, paySecret, certPath);
            WXPay wxpay = new WXPay(config);
            Map<String, String> hashMap = new HashMap<>(16);
            hashMap.put("appid", appId);
            hashMap.put("mch_id", mchId);
            hashMap.put("out_trade_no", orderId);
            hashMap.put("nonce_str", WXPayUtil.generateNonceStr());
            return wxpay.orderQuery(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取token
     *
     * @return token
     */
    public String getToken(String appId, String appSecret) {
        String token = "";
        //String key = "WeChatPayUtil_getToken_" + appId;
        //if (null != redisService.get(key)) {
        //    token = redisService.get(key).toString();
        //} else {
        //    // 授予形式
        //    String grantType = "client_credential";
        //    // 接口地址拼接参数(appId为微信服务号，secret为服务号的秘钥)
        //    String param = "grant_type=" + grantType + "&appid=" + appId + "&secret=" + appSecret;
        //    String tokenJsonStr = HttpRequestProxy.sendGet(WechatPayConfig.TOKEN_URL, param);
        //    if (StringUtils.isNotBlank(tokenJsonStr)) {
        //        JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
        //        token = tokenJson.getString("access_token");
        //        redisService.set(key, token, 60 * 60L);
        //    }
        //}
        return token;
    }

    /**
     * 获取门票
     *
     * @param token
     * @return
     */
    public String getTicket(String token) {
        String ticket = "";
        //String key = "WeChatPayUtil_getTicket_" + token;
        //if (null != redisService.get(key)) {
        //    ticket = redisService.get(key).toString();
        //} else {
        //    String param = "access_token=" + token + "&type=jsapi";
        //    String ticketJsonStr = HttpRequestProxy.sendGet(WechatPayConfig.TICKET_URL, param);
        //    if (StringUtils.isNotEmpty(ticketJsonStr)) {
        //        JSONObject ticketJson = JSONObject.parseObject(ticketJsonStr);
        //        ticket = ticketJson.getString("ticket");
        //        redisService.set(key, ticket, 60 * 60L);
        //    }
        //}
        return ticket;
    }

    /**
     * h5网页微信分享，获取参数
     *
     * @param url
     * @return
     */
    public Map<String, String> getShare(String url) {
        Map<String, String> hashMap = new HashMap<>(16);
        String appId = WechatPayConfig.APP_ID_JSAPI;
        String secret = WechatPayConfig.APP_SECRET_JSAPI;
        String nonceStr = WXPayUtil.generateNonceStr();
        String timestamp = WechatPayConfig.getCurrentTimestamp();
        String ticket = "";
        String signature = "";
        try {
            String token = getToken(appId, secret);
            ticket = getTicket(token);
            String param = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(param.getBytes(StandardCharsets.UTF_8));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            logger.error("WeChatPayUtil--getShare异常:", e);
        }
        hashMap.put("url", url);
        hashMap.put("jsapi_ticket", ticket);
        hashMap.put("nonceStr", nonceStr);
        hashMap.put("timestamp", timestamp);
        hashMap.put("signature", signature);
        hashMap.put("appId", appId);
        return hashMap;

    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 微信网页授权 获取登录凭证code
     * https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html
     * <p>
     * 小程序 获取登录凭证
     * https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/wx.login.html
     *
     * @param args
     */
    public static void main(String[] args) {


    }
}
