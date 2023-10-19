package com.nova.shopping.pay.service.pay;


import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.enums.PayWayEnum;
import com.nova.shopping.pay.entity.param.PayParam;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 支付策略实现类
 * (2)也可以实现InitializingBean，重写afterPropertiesSet方法，applicationContext获取PayService的实现类放入Map
 * @author: wzh
 * @date: 2023/3/20 17:46
 */
@Component
public class PayStrategy implements ApplicationContextAware {

    private final Map<PayWayEnum, PayService> payMap = new ConcurrentHashMap<>();

    /**
     * 项目启动初始化，把实现类放到map
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
     * @param param
     * @return
     */
    public AjaxResult pay(PayWayEnum payWayEnum, PayParam param) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.pay(param);
        }
        return null;
    }

    /**
     * 公共退款方法
     *
     * @param payWayEnum
     * @param param
     * @return
     */
    public AjaxResult refund(PayWayEnum payWayEnum, PayParam param) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.refund(param);
        }
        return null;
    }

    /**
     * 公共查询订单方法
     *
     * @param payWayEnum
     * @param param
     * @return
     */
    public AjaxResult queryOrder(PayWayEnum payWayEnum, PayParam param) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.queryOrder(param);
        }
        return null;
    }

    /**
     * 获取openId
     *
     * @param payWayEnum
     * @param param
     * @return
     */
    public AjaxResult getOpenId(PayWayEnum payWayEnum, PayParam param) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.getOpenId(param);
        }
        return null;
    }

    /**
     * 关闭订单
     *
     * @param payWayEnum
     * @param param
     * @return
     */
    public AjaxResult closeOrder(PayWayEnum payWayEnum, PayParam param) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.closeOrder(param);
        }
        return null;
    }

    /**
     * 商家转账到个人
     *
     * @param payWayEnum
     * @param param
     * @return
     */
    public AjaxResult merchantTransfer(PayWayEnum payWayEnum, PayParam param) {
        PayService payService = payMap.get(payWayEnum);
        if (null != payService) {
            return payService.merchantTransfer(param);
        }
        return null;
    }

}
