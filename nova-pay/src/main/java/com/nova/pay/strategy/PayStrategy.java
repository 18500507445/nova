package com.nova.pay.strategy;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 使用spirng的ApplicationContextAware把策略初始化到map里，对外提供resolvePay方法
 * @Author: wangzehui
 * @Date: 2022/9/18 11:41
 */
@Component
public class PayStrategy implements ApplicationContextAware {

    private final Map<PayWayEnum, PayService> payMap = new ConcurrentHashMap<>();

    /**
     * 把不同实现类放到map
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PayService> beansOfType = applicationContext.getBeansOfType(PayService.class);
        beansOfType.values().forEach(payService -> payMap.put(payService.getPayType(), payService));
    }

    /**
     * 公共支付方法
     *
     * @param payWayEnum
     * @param payParam
     * @return
     */
    public AjaxResult pay(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.pay(payParam);
        }
        return null;
    }

    /**
     * 公共退款方法
     *
     * @param payWayEnum
     * @param payParam
     * @return
     */
    public AjaxResult refund(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.refund(payParam);
        }
        return null;
    }

    /**
     * 公共查询订单方法
     *
     * @param payWayEnum
     * @param payParam
     * @return
     */
    public AjaxResult queryOrder(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.queryOrder(payParam);
        }
        return null;
    }

    /**
     * 获取openId
     *
     * @param payWayEnum
     * @param payParam
     * @return
     */
    public AjaxResult getOpenId(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.getOpenId(payParam);
        }
        return null;
    }

    /**
     * 关闭订单
     *
     * @param payWayEnum
     * @param payParam
     * @return
     */
    public AjaxResult closeOrder(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.closeOrder(payParam);
        }
        return null;
    }

    /**
     * 商家转账到个人
     *
     * @param payWayEnum
     * @param payParam
     * @return
     */
    public AjaxResult merchantTransfer(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.merchantTransfer(payParam);
        }
        return null;
    }
}
