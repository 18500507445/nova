package com.nova.pay.utils.open;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.nova.pay.entity.param.AliPayParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 支付宝配置
 * @Author: wangzehui
 * @Date: 2022/8/22 09:40
 */
@Component
public class AliPayUtil {

    private static final Logger logger = LoggerFactory.getLogger(AliPayUtil.class);

    /**
     * 支付宝网关地址： 沙箱环境用：https://openapi.alipaydev.com/gateway.do
     */
    public static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    public static final String SIGN_TYPE = "RSA2";

    public static final String FORMAT = "json";

    public static final String CHARSET = "UTF-8";

    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final String NOTIFY_FAIL = "fail";

    public static final String NOTIFY_SUCCESS = "success";

    /**
     * 默认连接方式
     *
     * @param appId      应用id
     * @param privateKey 支付宝应用私钥
     * @param publicKey  支付宝公钥
     * @return
     */
    public AlipayClient getDefaultClient(String appId, String privateKey, String publicKey) {
        return new DefaultAlipayClient(GATEWAY_URL, appId, privateKey, FORMAT, CHARSET, publicKey, SIGN_TYPE);
    }

    /**
     * H5请求支付宝支付
     * 文档地址：https://opendocs.alipay.com/apis/api_1/alipay.trade.wap.pay
     * 手机网站支付接口2.0
     *
     * @param param
     * @return
     */
    public AlipayTradeWapPayResponse aLiPayH5(AliPayParam param) {
        try {
            AlipayClient alipayClient = getDefaultClient(param.getAppId(), param.getPrivateKey(), param.getPublicKey());

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", param.getOutTradeNo());
            //单位元
            bizContent.put("total_amount", param.getTotalAmount());
            bizContent.put("subject", param.getSubject());
            bizContent.put("product_code", "QUICK_WAP_WAY");
            bizContent.put("body", param.getBody());

            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            request.setNotifyUrl(param.getNotifyUrl());
            request.setReturnUrl(param.getReturnUrl());
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
     * @param param
     * @return
     */
    public AlipayTradeAppPayResponse aLiPayApp(AliPayParam param) {
        try {
            AlipayClient alipayClient = getDefaultClient(param.getAppId(), param.getPrivateKey(), param.getPublicKey());
            JSONObject bizContent = new JSONObject();
            //(是)商户网站唯一订单号
            bizContent.put("out_trade_no", param.getOutTradeNo());
            //(是)订单总金额，单位为元，精确到小数点后两位
            bizContent.put("total_amount", param.getTotalAmount());
            //(是)商品的标题/交易标题/订单标题/订单关键字等
            bizContent.put("subject", param.getSubject());
            //(否)设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。
            bizContent.put("timeout_express", 30d);
            bizContent.put("quit_url", param.getReturnUrl());
            //(否)订单附加信息。如果请求时传递了该参数，将在异步通知、对账单中原样返回，同时会在商户和用户的pc账单详情中作为交易描述展示
            bizContent.put("body", param.getBody());
            //(是)销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
            bizContent.put("product_code", "QUICK_MSECURITY_PAY");

            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            request.setNotifyUrl(param.getNotifyUrl());
            request.setBizContent(bizContent.toString());
            request.setBizContent(bizContent.toString());
            request.setReturnUrl(param.getReturnUrl());
            return alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            logger.error("aLiPayApp异常:", e);
        }
        return null;
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
            result = AlipaySignature.rsaCheckV1(params, publicKey, AliPayUtil.CHARSET, AliPayUtil.SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询订单
     *
     * @param param
     * @return
     */
    public AlipayTradeQueryResponse queryOrder(AliPayParam param) {
        AlipayClient alipayClient;
        try {
            alipayClient = getDefaultClient(param.getAppId(), param.getPrivateKey(), param.getPublicKey());
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", param.getOutTradeNo());
            request.setBizContent(bizContent.toString());
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new AlipayTradeQueryResponse();
    }

    /**
     * 订单退款
     * 文档：https://opendocs.alipay.com/mini/02j1c6
     *
     * @param param
     * @return
     */
    public AlipayTradeRefundResponse refund(AliPayParam param) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(AliPayUtil.GATEWAY_URL, param.getAppId(), param.getPrivateKey(),
                    AliPayUtil.FORMAT, AliPayUtil.CHARSET, param.getPublicKey(), AliPayUtil.SIGN_TYPE);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", param.getOutTradeNo());
            bizContent.put("refund_amount", param.getTotalAmount());
            bizContent.put("refund_reason", param.getReason());
            request.setBizContent(bizContent.toString());
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new AlipayTradeRefundResponse();
    }

    /**
     * 关闭订单
     * 文档：https://opendocs.alipay.com/open/02e7gn
     *
     * @param param
     */
    public AlipayTradeCloseResponse closeOrder(AliPayParam param) {
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(AliPayUtil.GATEWAY_URL, param.getAppId(), param.getPrivateKey(),
                    AliPayUtil.FORMAT, AliPayUtil.CHARSET, param.getPublicKey(), AliPayUtil.SIGN_TYPE);
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("trade_no", param.getOutTradeNo());
            request.setBizContent(bizContent.toString());
            return alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new AlipayTradeCloseResponse();
    }


    public static void main(String[] args) {


    }
}
