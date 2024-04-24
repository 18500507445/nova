package com.nova.shopping.pay.service.strategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.repository.entity.PayConfig;
import com.nova.shopping.pay.repository.entity.PayOrder;
import com.nova.shopping.pay.web.dto.PayParam;
import com.nova.shopping.pay.web.dto.YeePayParam;
import com.nova.shopping.pay.payment.open.YeePayment;
import com.nova.shopping.pay.service.pay.PayConfigService;
import com.nova.shopping.pay.service.pay.PayOrderService;
import com.nova.shopping.pay.service.strategy.PayService;
import com.yeepay.shade.org.apache.commons.collections4.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description: 易宝支付实现类
 * @author: wzh
 * @date: 2023/3/20 17:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class YeePayServiceImpl implements PayService {

    private final YeePayment yeePayment;

    private final PayOrderService payOrderService;

    private final PayConfigService payConfigService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.YEE_PAY;
    }

    /**
     * 易宝一级
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult pay(PayParam param) {
        JSONObject result = new JSONObject();
        String body = "";
        //入支付流水 1H5支付,2小程序,3app支付 4jsapi微信原生
        int type = Integer.parseInt(param.getType());
        String source = param.getSource();
        String sid = param.getSid();
        String userName = param.getUserName();
        String totalAmount = param.getTotalAmount();
        String orderId = param.getOrderId();
        Long payConfigId = param.getPayConfigId();
        String productId = param.getProductId();
        String subject = param.getSubject();
        int businessCode = param.getBusinessCode();
        if (ObjectUtil.hasEmpty(source, sid, payConfigId, orderId, type, userName, totalAmount, productId, subject, businessCode)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            PayOrder payOrder = payOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 4);
            if (ObjectUtil.isNotNull(payOrder)) {
                return AjaxResult.error("1000", "订单已存在,请从新下单");
            } else {
                //1.0 获取支付配置
                PayConfig payConfig = payConfigService.getConfigData(payConfigId);
                if (ObjectUtil.isNull(payConfig)) {
                    return AjaxResult.error("1000", "没有查询到支付方式");
                }
                PayOrder insert = PayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(4)
                        .type(type)
                        .businessCode(businessCode)
                        .fee(new BigDecimal(totalAmount)).build();

                //2.0 创建订单
                YeePayParam.YeePayParamBuilder yeePayBuilder = YeePayParam.builder().appKey(payConfig.getAppId())
                        .privateKey(payConfig.getPrivateKey())
                        .parentMerchantNo(payConfig.getMchId())
                        .merchantNo(payConfig.getMchId())
                        .orderId(orderId)
                        .amount(totalAmount)
                        .notifyUrl(payConfig.getNotifyUrl())
                        .returnUrl(param.getReturnUrl())
                        .goodsName(subject)
                        .userName(userName);

                Map<String, String> orderMap = yeePayment.tradeOrder(yeePayBuilder.build());
                if (!orderMap.isEmpty() && StringUtils.equals(Constants.YEE_CODE, MapUtils.getString(orderMap, "code"))) {
                    String token = MapUtils.getString(orderMap, "token");
                    String uniqueOrderNo = MapUtils.getString(orderMap, "uniqueOrderNo");
                    yeePayBuilder.token(token);
                    insert.setSign(token);
                    insert.setTradeNo(uniqueOrderNo);
                    insert.setRemark(payConfig.getMchId());
                    int flag = payOrderService.insertMyPayOrder(insert);
                    if (0 == flag) {
                        return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                    }
                    //3.0 唤起收银台
                    body = yeePayment.cashier(yeePayBuilder.build());
                    if (StringUtils.isNotBlank(body)) {
                        result.put("body", body);
                    }
                } else {
                    log.debug("yeePayOrderMap：{}", JSONUtil.toJsonStr(orderMap));
                }
            }
        } catch (Exception e) {
            log.error("yeePay异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult refund(PayParam param) {
        String orderId = param.getOrderId();
        Long payConfigId = param.getPayConfigId();
        String totalAmount = param.getTotalAmount();
        if (ObjectUtil.hasEmpty(payConfigId, orderId, totalAmount)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        PayConfig payConfig = payConfigService.selectMyPayConfigById(payConfigId);
        PayOrder order = payOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, param.getPayWay());
        if (!ObjectUtil.hasNull(payConfig, order)) {
            YeePayParam yeePayParam = YeePayParam.builder().appKey(payConfig.getAppId())
                    .privateKey(payConfig.getPrivateKey())
                    .parentMerchantNo(payConfig.getMchId())
                    .merchantNo(payConfig.getMchId())
                    .orderId(orderId)
                    .amount(totalAmount)
                    .tradeNo(order.getTradeNo())
                    .build();
            AjaxResult.success(yeePayment.refund(yeePayParam));
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        Integer payWay = param.getPayWay();
        String orderId = param.getOrderId();
        if (ObjectUtil.hasEmpty(orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        PayOrder payOrder = payOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, payWay);
        if (ObjectUtil.isNull(payOrder)) {
            return AjaxResult.error("1000", "没有查询到订单信息");
        }
        //获取支付配置
        PayConfig payConfig = payConfigService.getConfigData(payOrder.getPayConfigId());
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        YeePayParam yeePayParam = YeePayParam.builder().appKey(payConfig.getAppId())
                .privateKey(payConfig.getPrivateKey())
                .parentMerchantNo(payConfig.getMchId())
                .merchantNo(payConfig.getMchId())
                .orderId(orderId)
                .build();
        return AjaxResult.success(yeePayment.refund(yeePayParam));
    }

    @Override
    public AjaxResult getOpenId(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult closeOrder(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }

    @Override
    public AjaxResult merchantTransfer(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }
}
