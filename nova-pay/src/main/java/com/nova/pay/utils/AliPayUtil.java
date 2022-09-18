package com.nova.pay.utils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.nova.pay.config.AliPayConfig;
import com.nova.pay.entity.data.AliPayData;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.util.Map;

/**
 * @Description: 支付宝工具类
 * @Author: wangzehui
 * @Date: 2022/6/17 13:11
 */
@Component
public class AliPayUtil {

    private static final Logger logger = LoggerFactory.getLogger(AliPayUtil.class);

    /**
     * H5请求支付宝支付
     * 文档地址：https://opendocs.alipay.com/apis/api_1/alipay.trade.wap.pay
     * 手机网站支付接口2.0
     * 默认支付方式：AliPayConfig.getDefaultClient();
     *
     * @param aliPayData
     * @return
     */
    public AlipayTradeWapPayResponse aLiPayH5(AliPayData aliPayData) {
        try {
            String appId = aliPayData.getAppId();
            String privateKey = aliPayData.getPrivateKey();
            String publicKey = aliPayData.getPublicKey();
            AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.GATEWAY_URL, appId, privateKey, AliPayConfig.FORMAT, AliPayConfig.CHARSET, publicKey, AliPayConfig.SIGN_TYPE);
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", aliPayData.getOutTradeNo());
            //单位元
            bizContent.put("total_amount", aliPayData.getTotalAmount());
            bizContent.put("subject", aliPayData.getSubject());
            bizContent.put("product_code", "QUICK_WAP_WAY");
            bizContent.put("body", aliPayData.getBody());

            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            request.setNotifyUrl(AliPayConfig.NOTIFY_URL);
            if (StringUtils.isNotBlank(aliPayData.getReturnUrl())) {
                request.setReturnUrl(URLDecoder.decode(aliPayData.getReturnUrl(), "utf-8"));
            } else {
                request.setReturnUrl(aliPayData.getReturnUrl());
            }
            request.setBizContent(bizContent.toString());
            return alipayClient.pageExecute(request);
        } catch (Exception e) {
            logger.error("aLiPayH5异常:", e);
        }
        return null;
    }

    /**
     * app请求支付宝支付
     *
     * @param aliPayData
     * @return
     */
    public AlipayTradeAppPayResponse aLiPayApp(AliPayData aliPayData) {
        try {
            AlipayClient alipayClient = AliPayConfig.getDefaultClient();

            JSONObject bizContent = new JSONObject();
            //(是)商户网站唯一订单号
            bizContent.put("out_trade_no", aliPayData.getOutTradeNo());
            //(是)订单总金额，单位为元，精确到小数点后两位
            bizContent.put("total_amount", aliPayData.getTotalAmount());
            //(是)商品的标题/交易标题/订单标题/订单关键字等
            bizContent.put("subject", aliPayData.getSubject());
            //(否)设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。
            bizContent.put("timeout_express", 30d);
            bizContent.put("quit_url", aliPayData.getReturnUrl());
            //(否)订单附加信息。如果请求时传递了该参数，将在异步通知、对账单中原样返回，同时会在商户和用户的pc账单详情中作为交易描述展示
            bizContent.put("body", aliPayData.getBody());
            //(是)销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
            bizContent.put("product_code", "QUICK_MSECURITY_PAY");

            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            request.setNotifyUrl(AliPayConfig.NOTIFY_URL);
            request.setBizContent(bizContent.toString());
            request.setBizContent(bizContent.toString());
            request.setReturnUrl(aliPayData.getReturnUrl());
            return alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            logger.error("aLiPayApp异常:", e);
        }
        return null;
    }

    /**
     * 小程序请求支付宝支付
     * 文档地址：https://opendocs.alipay.com/mini/02j1c2
     *
     * @param aliPayData
     * @return
     */
    public AlipayTradeCreateResponse aLiPayApplet(AliPayData aliPayData) {
        try {
            AlipayClient alipayClient = AliPayConfig.getAppleClient();

            JSONObject bizContent = new JSONObject();
            //订单号
            bizContent.put("out_trade_no", aliPayData.getOutTradeNo());
            //金额 这里的金额是以元为单位的可以不转换但必须是字符串
            bizContent.put("total_amount", aliPayData.getTotalAmount());
            //描述
            bizContent.put("subject", aliPayData.getSubject());
            //用户唯一标识id 这里必须使用buyer_id 参考文档
            bizContent.put("buyer_id", aliPayData.getUserId());
            bizContent.put("body", aliPayData.getBody());

            AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
            //对象转化为json字符串,商户通过该接口进行交易的创建下单
            request.setBizContent(bizContent.toString());
            //回调地址 是能够访问到的域名加上方法名
            request.setNotifyUrl(AliPayConfig.NOTIFY_URL);
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            logger.error("aLiPayApplet异常:", e);
        }
        return null;
    }

    /**
     * 支付宝小程序支付获取userId
     *
     * @param aliPayData
     * @return
     */
    public AlipaySystemOauthTokenResponse getUserId(AliPayData aliPayData) {
        AlipaySystemOauthTokenResponse response = new AlipaySystemOauthTokenResponse();
        try {
            AlipayClient alipayClient = AliPayConfig.getAppleClient();

            //小程序比较特殊需要先拿authCode
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            // 值为authorization_code时，代表用code换取
            request.setGrantType("authorization_code");
            //授权码，用户对应用授权后得到的
            request.setCode(aliPayData.getAuthCode());
            //这里使用execute方法
            response = alipayClient.execute(request);
            //刷新令牌，上次换取访问令牌时得到。见出参的refresh_token字段
            request.setRefreshToken(response.getAccessToken());
        } catch (AlipayApiException e) {
            logger.error("getUserId异常:", e);
        }
        return response;
    }

    /**
     * 默认验签
     * 支付回来校验签名,防止恶意回调
     *
     * @param params
     * @return
     */
    public boolean rsaCheckV1(Map<String, String> params, String publicKey) {
        boolean result = false;
        try {
            String appId = MapUtils.getString(params, "app_id");
            String type = MapUtils.getString(params, "body");
            result = AlipaySignature.rsaCheckV1(params, publicKey, AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 证书验签
     * 支付回来校验签名,防止恶意回调
     *
     * @param params
     * @return
     */
    public boolean rsaCertCheckV1(Map<String, String> params) {
        boolean result = false;
        try {
            result = AlipaySignature.rsaCertCheckV1(params, AliPayConfig.PUBLIC_CERT_PATH,
                    AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 订单退款
     * 文档：https://opendocs.alipay.com/mini/02j1c6
     *
     * @param tradeNo 商户交易订单号
     * @return
     */
    public AlipayTradeRefundResponse refund(String appId, String tradeNo, String refundAmount,
                                            String refundReason, String privateKey, String publicKey) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.GATEWAY_URL, appId, privateKey,
                    AliPayConfig.FORMAT, AliPayConfig.CHARSET, publicKey, AliPayConfig.SIGN_TYPE);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", tradeNo);
            bizContent.put("refund_amount", refundAmount);
            bizContent.put("refund_reason", refundReason);
            request.setBizContent(bizContent.toString());
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new AlipayTradeRefundResponse();
    }

    /**
     * 查询订单
     * 使用场景：调起支付超时，通知成功，但是通知响应超时，一般这种回调不会有重试通知了
     *
     * @param appId
     * @param orderId
     * @return
     */
    public AlipayTradeQueryResponse queryOrder(String appId, String orderId, String publicKey, String privateKey) {
        AlipayClient alipayClient;
        try {
            if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(publicKey) && StringUtils.isNotBlank(privateKey)) {
                alipayClient = new DefaultAlipayClient(AliPayConfig.GATEWAY_URL, appId, privateKey,
                        AliPayConfig.FORMAT, AliPayConfig.CHARSET, publicKey, AliPayConfig.SIGN_TYPE);
            } else {
                alipayClient = AliPayConfig.getAppleClient();
            }
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderId);
            request.setBizContent(bizContent.toString());
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new AlipayTradeQueryResponse();
    }

    /**
     * 支付宝对账单
     *
     * @param appId      应用ID
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @param date       账单日期 YYYY-MM-DD
     */
    public void billDownloadUrlQuery(String appId, String privateKey, String publicKey, String date) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.GATEWAY_URL, appId, privateKey,
                    AliPayConfig.FORMAT, AliPayConfig.CHARSET, publicKey, AliPayConfig.SIGN_TYPE);
            AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("bill_type", "signcustomer");
            bizContent.put("bill_date", date);
            request.setBizContent(bizContent.toString());
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                System.out.println(response.getBillDownloadUrl());
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * main
     *
     * @param args
     */
    public static void main(String[] args) {

    }


}