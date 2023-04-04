package com.nova.pay.service.pay;

import com.nova.common.core.model.result.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 实现方式二
 *
 * 还有一种实现方式：支付抽象类
 * 多种支付方式继承抽象类，然后进行注入，Controller初始化map
 * 可以参考LockSeatServiceImpl，代码没有接口这种简洁
 *
 * @author: wzh
 * @date: 2023/1/16 22:47
 */
@Component
public class PayStrategyTwo implements InitializingBean {

    @Resource
    private List<PayService> list;

    private final Map<PayWayEnum, PayService> payMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        for (PayService payService : list) {
            payMap.put(payService.getPayType(), payService);
        }
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
}
