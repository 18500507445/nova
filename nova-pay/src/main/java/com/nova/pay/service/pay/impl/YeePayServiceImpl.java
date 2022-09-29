package com.nova.pay.service.pay.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.nova.common.core.domain.AjaxResult;
import com.nova.common.constant.PayConstants;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.entity.param.YeePayParam;
import com.nova.pay.entity.result.FkPayConfig;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.service.fk.FkPayConfigService;
import com.nova.pay.service.fk.FkPayOrderService;
import com.nova.pay.service.pay.PayService;
import com.nova.pay.utils.open.YeePayUtil;
import com.yeepay.shade.org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Description: 易宝支付实现类
 * @Author: wangzehui
 * @Date: 2022/9/20 17:45
 */
@Service
public class YeePayServiceImpl implements PayService {

    private static final Logger logger = LoggerFactory.getLogger(YeePayServiceImpl.class);

    @Autowired
    private YeePayUtil yeePayUtil;

    @Autowired
    private FkPayOrderService fkPayOrderService;

    @Autowired
    private FkPayConfigService fkPayConfigService;

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
        if (ObjectUtil.hasEmpty(source, sid, payConfigId, orderId, type, userName, totalAmount, productId, subject)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 4);
            if (ObjectUtil.isNotNull(payOrder)) {
                return AjaxResult.error("1000", "订单已存在,请从新下单");
            } else {
                //1.0 获取支付配置
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
                        .payWay(4)
                        .type(type)
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

                Map<String, String> orderMap = yeePayUtil.tradeOrder(yeePayBuilder.build());
                if (!orderMap.isEmpty() && StringUtils.equals(PayConstants.YEE_CODE, MapUtils.getString(orderMap, "code"))) {
                    String token = MapUtils.getString(orderMap, "token");
                    String uniqueOrderNo = MapUtils.getString(orderMap, "uniqueOrderNo");
                    yeePayBuilder.token(token);
                    insert.setSign(token);
                    insert.setTradeNo(uniqueOrderNo);
                    insert.setRemark(payConfig.getMchId());
                    int flag = fkPayOrderService.insertFkPayOrder(insert);
                    if (0 == flag) {
                        return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                    }
                    //3.0 唤起收银台
                    body = yeePayUtil.cashier(yeePayBuilder.build());
                    if (StringUtils.isNotBlank(body)) {
                        result.put("body", body);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("yeePay异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult refund(PayParam param) {
        Object result = "";
        String orderId = param.getOrderId();
        Long payConfigId = param.getPayConfigId();
        String totalAmount = param.getTotalAmount();
        if (ObjectUtil.hasEmpty(payConfigId, orderId, totalAmount)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        FkPayConfig payConfig = fkPayConfigService.selectFkPayConfigById(payConfigId);
        FkPayOrder order = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, param.getPayWay());
        if (!ObjectUtil.hasNull(payConfig, order)) {
            YeePayParam yeePayParam = YeePayParam.builder().appKey(payConfig.getAppId())
                    .privateKey(payConfig.getPrivateKey())
                    .parentMerchantNo(payConfig.getMchId())
                    .merchantNo(payConfig.getMchId())
                    .orderId(orderId)
                    .amount(totalAmount)
                    .tradeNo(order.getTradeNo())
                    .build();
            Map<String, String> refundMap = yeePayUtil.refund(yeePayParam);
            if (!refundMap.isEmpty()) {
                result = JSONUtil.toJsonStr(refundMap);
            }
        }
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult queryOrder(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
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
