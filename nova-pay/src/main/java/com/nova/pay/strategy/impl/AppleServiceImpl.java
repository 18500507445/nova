package com.nova.pay.strategy.impl;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.strategy.PayService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/9/20 17:44
 */
@Service
public class AppleServiceImpl implements PayService {

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.APPLE;
    }

    @Override
    public AjaxResult pay(PayParam payParam) {
        return null;
    }
}
