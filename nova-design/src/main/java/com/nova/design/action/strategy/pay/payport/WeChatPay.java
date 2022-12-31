package com.nova.design.action.strategy.pay.payport;


import com.nova.design.action.strategy.pay.entity.PayState;

/**
 * @description: 微信具体支付策略
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class WeChatPay implements Payment {

    @Override
    public PayState pay(String uid, double amount) {
        System.out.println("欢迎使用微信支付");
        System.out.println("直接从微信红包扣款");
        return new PayState(200, "支付成功", String.valueOf(amount));
    }
}
