package com.nova.pay.service.pay.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.AliPayParam;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.service.fk.FkPayConfigService;
import com.nova.pay.service.fk.FkPayOrderService;
import com.nova.pay.service.pay.PayService;
import com.nova.pay.utils.open.AliPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Description: 支付宝实现类
 * @Author: wangzehui
 * @Date: 2022/9/20 17:42
 */
@Slf4j
@Service
public class AliServiceImpl implements PayService {

    @Autowired
    private AliPayUtil aliPayUtil;

    @Autowired
    private FkPayOrderService fkPayOrderService;

    @Autowired
    private FkPayConfigService fkPayConfigService;

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
        if (ObjectUtil.hasEmpty(source, sid, payConfigId, orderId, type, userName, totalAmount, productId, subject)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 1);
            if (ObjectUtil.isNotNull(payOrder)) {
                return AjaxResult.error("1000", "订单已存在,请从新下单");
            } else {
                //获取支付配置
                FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
                if (ObjectUtil.isNull(payConfig)) {
                    return AjaxResult.error("1000", "没有查询到支付方式");
                }
                FkPayOrder insert = FkPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(1)
                        .type(type)
                        .fee(new BigDecimal(totalAmount)).build();
                int flag = fkPayOrderService.insertFkPayOrder(insert);
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
                    response = aliPayUtil.aLiPayH5(data);
                } else if (ArrayUtil.contains(new int[]{3}, type)) {
                    response = aliPayUtil.aLiPayApp(data);
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
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                .publicKey(payConfig.getPublicKey())
                .privateKey(payConfig.getPrivateKey())
                .outTradeNo(orderId)
                .totalAmount(totalAmount)
                .reason("退款").build();
        AlipayTradeRefundResponse response = aliPayUtil.refund(aliPayParam);
        return AjaxResult.success(response.getSubMsg(), response.getBody());
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        String orderId = param.getOrderId();
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(payConfigId, orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                .publicKey(payConfig.getPublicKey())
                .privateKey(payConfig.getPrivateKey())
                .outTradeNo(orderId).build();
        AlipayTradeQueryResponse response = aliPayUtil.queryOrder(aliPayParam);
        return AjaxResult.success(response.getSubMsg(), response.getBody());
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
        FkPayConfig payConfig = fkPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AliPayParam aliPayParam = AliPayParam.builder().appId(payConfig.getAppId())
                .publicKey(payConfig.getPublicKey())
                .privateKey(payConfig.getPrivateKey())
                .outTradeNo(orderId).build();
        AlipayTradeCloseResponse response = aliPayUtil.closeOrder(aliPayParam);
        return AjaxResult.success(response.getSubMsg(), response.getBody());
    }

    @Override
    public AjaxResult merchantTransfer(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }
}
