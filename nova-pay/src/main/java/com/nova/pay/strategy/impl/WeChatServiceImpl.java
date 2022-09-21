package com.nova.pay.strategy.impl;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.strategy.PayService;
import com.nova.pay.enums.PayWayEnum;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Description: 微信支付实现类
 * @Author: wangzehui
 * @Date: 2022/9/18 11:37
 */
@Service
public class WeChatServiceImpl implements PayService {

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.WECHAT;
    }

    @Override
    public AjaxResult pay(PayParam payParam) {
        return AjaxResult.success("1000","微信支付",null);
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
