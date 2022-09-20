package com.nova.pay.strategy.impl;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.strategy.PayService;
import com.nova.pay.enums.PayWayEnum;
import org.springframework.stereotype.Component;

/**
 * @Description: 阿里支付实现类
 * @Author: wangzehui
 * @Date: 2022/9/18 11:37
 */
@Component
public class AliServiceImpl implements PayService {

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.ALI;
    }

    @Override
    public AjaxResult pay(PayParam payParam) {
        return AjaxResult.success("阿里支付");
    }
}
