package com.nova.pay.service.pay.impl;

import cn.hutool.core.util.ObjectUtil;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.entity.result.FkPayOrder;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.service.fk.FkPayOrderService;
import com.nova.pay.service.pay.PayService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @description: 苹果支付实现类
 * @author: wzh
 * @date: 2022/9/20 17:44
 */
@Slf4j
@Service
public class AppleServiceImpl implements PayService {

    @Autowired
    private FkPayOrderService fkPayOrderService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.APPLE;
    }

    /**
     * 苹果pay
     * <p>
     * 苹果付款：
     * 1.app调用服务器，服务器需返回交易流水号，存储支付流水即可
     * 2.交易后，苹果通知app，app通知服务器，服务器拿到小票信息 跟苹果进行验证
     * 3.验签通过，根据通知状态修改 支付流水表状态、订单表状态
     * <p>
     * 苹果退款：
     * 1.用户退款后,苹果通知服务器,修改支付流水表状态、订单表状态
     */
    @Override
    public AjaxResult pay(PayParam param) {
        //3app支付,5ios沙盒
        int type = Integer.parseInt(param.getType());
        String source = param.getSource();
        String sid = param.getSid();
        String userName = param.getUserName();
        String totalAmount = param.getTotalAmount();
        String orderId = param.getOrderId();
        String productId = param.getProductId();
        Long payConfigId = param.getPayConfigId();
        int businessCode = param.getBusinessCode();
        if (ObjectUtil.hasEmpty(source, sid, orderId, type, userName, totalAmount, productId, payConfigId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            FkPayOrder payOrder = fkPayOrderService.selectNtPayOrderByOrderIdAndPayWay(orderId, 3);
            if (ObjectUtil.isNull(payOrder)) {
                FkPayOrder insert = FkPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(3)
                        .type(type)
                        .businessCode(businessCode)
                        .fee(new BigDecimal(totalAmount)).build();
                int flag = fkPayOrderService.insertFkPayOrder(insert);
                if (0 == flag) {
                    return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                }
            } else {
                if (0 != payOrder.getTradeStatus()) {
                    return AjaxResult.error("1000", "支付结果确认中，请稍候");
                }
            }
        } catch (Exception e) {
            log.error("applePay异常：{}", e.getMessage());
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult refund(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
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
