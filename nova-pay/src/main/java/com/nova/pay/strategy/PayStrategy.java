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

    public AjaxResult pay(PayWayEnum payWayEnum, PayParam payParam) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.pay(payParam);
        }
        return null;
    }

    /**
     * 把不同策略放到map
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, PayService> beansOfType = applicationContext.getBeansOfType(PayService.class);
        beansOfType.values().forEach(payService -> payMap.put(payService.getPayType(), payService));
    }
}
