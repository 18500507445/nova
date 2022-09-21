package com.nova.pay.strategy.impl;


import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.strategy.PayService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/9/20 17:45
 */
@Service
public class YeePayServiceImpl implements PayService {

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.YEE_PAY;
    }

    @Override
    public AjaxResult pay(PayParam payParam) {
        return null;
    }

    @Override
    public AjaxResult refund(PayParam payParam) {
        return null;
    }

    @Override
    public AjaxResult queryOrder(PayParam payParam) {
        return null;
    }

    @Override
    public AjaxResult getOpenId(PayParam payParam) {
        return null;
    }

    @Override
    public AjaxResult closeOrder(PayParam payParam) {
        return null;
    }

    @Override
    public AjaxResult merchantTransfer(PayParam payParam) {
        return null;
    }
}
