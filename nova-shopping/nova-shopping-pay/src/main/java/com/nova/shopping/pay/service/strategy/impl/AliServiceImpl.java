package com.nova.shopping.pay.service.strategy.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.entity.MyPayConfig;
import com.nova.shopping.pay.entity.MyPayOrder;
import com.nova.shopping.pay.entity.param.AliPayParam;
import com.nova.shopping.pay.entity.param.PayParam;
import com.nova.shopping.pay.payment.open.AliPayment;
import com.nova.shopping.pay.service.pay.MyPayConfigService;
import com.nova.shopping.pay.service.pay.MyPayOrderService;
import com.nova.shopping.pay.service.strategy.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @description: 支付宝实现类
 * @author: wzh
 * @date: 2023/3/20 17:42
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliServiceImpl implements PayService {

    private final AliPayment aliPayment;

    private final MyPayOrderService myPayOrderService;

    private final MyPayConfigService myPayConfigService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.ALI;
    }

    @Override
    public AjaxResult pay(PayParam param) {
        JSONObject result = new JSONObject();
        String body = "";
        String source = param.getSource();
        String sid = param.getSid();
        //1H5支付,3app支付
        int type = Integer.parseInt(param.getType());
        String totalAmount = param.getTotalAmount();
        String orderId = param.getOrderId();
        String userName = param.getUserName();
        Long payConfigId = param.getPayConfigId();
        String productId = param.getProductId();
        String subject = param.getSubject();
        int businessCode = param.getBusinessCode();
        if (ObjectUtil.hasEmpty(source, sid, payConfigId, orderId, type, userName, totalAmount, productId, subject, businessCode)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 1);
            if (ObjectUtil.isNotNull(payOrder)) {
                return AjaxResult.error("1000", "订单已存在,请从新下单");
            } else {
                //获取支付配置
                MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
                if (ObjectUtil.isNull(payConfig)) {
                    return AjaxResult.error("1000", "没有查询到支付方式");
                }
                MyPayOrder insert = MyPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(1)
                        .type(type)
                        .businessCode(businessCode)
                        .fee(new BigDecimal(totalAmount)).build();
                int flag = myPayOrderService.insertMyPayOrder(insert);
                if (0 == flag) {
                    return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                }
                AliPayParam data = AliPayParam.builder().appId(payConfig.getAppId())
                        .publicKey(payConfig.getPublicKey())
                        .privateKey(payConfig.getPrivateKey())
                        .outTradeNo(orderId)
                        .subject(subject)
                        .totalAmount(totalAmount)
                        .body(String.valueOf(type))
                        .returnUrl(param.getReturnUrl())
                        .notifyUrl(payConfig.getNotifyUrl()).build();
                AlipayResponse response = null;
                if (ArrayUtil.contains(new int[]{1}, type)) {
                    response = aliPayment.aLiPayH5(data);
                } else if (ArrayUtil.contains(new int[]{3}, type)) {
                    response = aliPayment.aLiPayApp(data);
                } else if (ArrayUtil.contains(new int[]{11}, type)) {
                    response = aliPayment.aLiPayQr(data);
                }
                if (ObjectUtil.isNotNull(response) && response.isSuccess()) {
                    body = response.getBody();
                }
                result.put("body", body);
            }
        } catch (Exception e) {
            log.error("aLiPay异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult refund(PayParam param) {
        String orderId = param.getOrderId();
        String totalAmount = param.getTotalAmount();
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(payConfigId, orderId, totalAmount)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                .publicKey(payConfig.getPublicKey())
                .privateKey(payConfig.getPrivateKey())
                .outTradeNo(orderId)
                .totalAmount(totalAmount)
                .reason("退款").build();
        AlipayTradeRefundResponse response = aliPayment.refund(aliPayParam);
        return AjaxResult.success(response.getSubMsg(), response);
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        String orderId = param.getOrderId();
        Integer payWay = param.getPayWay();
        if (ObjectUtil.hasEmpty(orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, payWay);
        if (ObjectUtil.isNull(payOrder)) {
            return AjaxResult.error("1000", "没有查询到订单信息");
        }
        MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                .publicKey(payConfig.getPublicKey())
                .privateKey(payConfig.getPrivateKey())
                .outTradeNo(orderId).build();
        return AjaxResult.success(aliPayment.queryOrder(aliPayParam));
    }

    @Override
    public AjaxResult getOpenId(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult closeOrder(PayParam param) {
        Long payConfigId = param.getPayConfigId();
        String orderId = param.getOrderId();
        if (ObjectUtil.hasEmpty(payConfigId, orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                .publicKey(payConfig.getPublicKey())
                .privateKey(payConfig.getPrivateKey())
                .outTradeNo(orderId).build();
        AlipayTradeCloseResponse response = aliPayment.closeOrder(aliPayParam);
        return AjaxResult.success(response.getSubMsg(), response.getBody());
    }

    @Override
    public AjaxResult merchantTransfer(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }
}
