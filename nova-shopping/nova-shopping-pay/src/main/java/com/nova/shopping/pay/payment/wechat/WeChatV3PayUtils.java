package com.nova.shopping.pay.payment.wechat;

import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall.pay.payment.open.WeChatPayment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 微信支付v3工具类
 * @author: wzh
 * @date: 2023/3/14 22:17
 */
@Component
public class WeChatV3PayUtils {

    @Resource
    private WeChatPayment weChatPayment;

    public static void main(String[] args) throws WxPayException {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(System.currentTimeMillis() + "");
        request.setNotifyUrl("https://www.baidu.com");
        request.setDescription("wzh");

        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setCurrency("CNY");
        amount.setTotal(100);
        request.setAmount(amount);

        WeChatPayment weChatPayment = new WeChatPayment();
        WxPayUnifiedOrderV3Result.AppResult result = weChatPayment.getWxV3PayService().createOrderV3(TradeTypeEnum.APP, request);
        System.out.println(JSONUtil.toJsonStr(result));
    }


    /**
     * 调用统一下单接口，并组装生成支付所需参数对象.
     */
    public Object createOrder(String[] args) throws WxPayException {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(System.currentTimeMillis() + "");
        request.setNotifyUrl("https://www.baidu.com");
        request.setDescription("wzh");

        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(System.currentTimeMillis() + "");
        request.setPayer(payer);

        //构建金额信息
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        //设置币种信息
        amount.setCurrency("CNY");
        //设置金额
        amount.setTotal(100);
        request.setAmount(amount);

        return weChatPayment.getWxV3PayService().<WxPayUnifiedOrderV3Result.AppResult>createOrderV3(TradeTypeEnum.APP, request);
    }

    /**
     * 查询订单
     */
    public void queryOrder(String outTradeNo) throws WxPayException {
        WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
        request.setOutTradeNo(outTradeNo);
        WxPayOrderQueryV3Result result = weChatPayment.getWxV3PayService().queryOrderV3(request);
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
        weChatPayment.getWxV3PayService().closeOrderV3(request);
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
        WxPayRefundV3Result result = weChatPayment.getWxV3PayService().refundV3(request);
    }

    /**
     * 查询退款
     */
    public void queryRefund(String orderId) throws WxPayException {
        WxPayRefundQueryV3Request request = new WxPayRefundQueryV3Request();
        request.setOutRefundNo(orderId);
        WxPayRefundQueryV3Result result = weChatPayment.getWxV3PayService().refundQueryV3(request);
    }

}
