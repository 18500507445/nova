package com.nova.pay.untils;

import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.nova.pay.config.WeChatConfig;

/**
 * @Description: 微信支付v3工具类
 * @Author: wangzehui
 * @Date: 2022/8/16 20:32
 */
public class WeChatV3PayUtils {

    public static final WxPayService wxService = WeChatConfig.getWxV3PayService();

    /**
     * 调用统一下单接口，并组装生成支付所需参数对象.
     */
    public void createOrder() throws WxPayException {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo("");
        request.setNotifyUrl("");
        request.setDescription("test");

        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid("openid");
        request.setPayer(payer);

        //构建金额信息
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        //设置币种信息
        amount.setCurrency("CNY");
        //设置金额
        amount.setTotal(1);
        request.setAmount(amount);

        WxPayUnifiedOrderV3Result.JsapiResult result = wxService.createOrderV3(TradeTypeEnum.JSAPI, request);
    }

    /**
     * 查询订单
     */
    public void queryOrder(String outTradeNo) throws WxPayException {
        WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
        request.setOutTradeNo(outTradeNo);
        WxPayOrderQueryV3Result result = wxService.queryOrderV3(request);
    }

    /**
     * 关闭订单
     *
     * @param outTradeNo
     * @throws WxPayException
     */
    public void closeOrder(String outTradeNo) throws WxPayException {
        WxPayOrderCloseV3Request request = new WxPayOrderCloseV3Request();
        request.setOutTradeNo(outTradeNo);
        wxService.closeOrderV3(request);
    }


    /**
     * 退款
     *
     * @param outTradeNo
     * @param orderId
     * @throws WxPayException
     */
    public void refund(String outTradeNo, String orderId) throws WxPayException {
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        request.setOutTradeNo(outTradeNo);
        request.setOutRefundNo(orderId);
        request.setNotifyUrl("");
        request.setAmount(new WxPayRefundV3Request.Amount().setRefund(100).setTotal(100).setCurrency("CNY"));
        WxPayRefundV3Result result = wxService.refundV3(request);
    }

    /**
     * 查询退款
     */
    public void queryRefund(String orderId) throws WxPayException {
        WxPayRefundQueryV3Request request = new WxPayRefundQueryV3Request();
        request.setOutRefundNo(orderId);
        WxPayRefundQueryV3Result result = wxService.refundQueryV3(request);
    }

}