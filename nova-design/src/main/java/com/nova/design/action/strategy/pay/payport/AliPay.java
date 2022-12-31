package com.nova.design.action.strategy.pay.payport;


import com.nova.design.action.strategy.pay.entity.PayState;

/**
 * @description: 具体支付策略
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AliPay implements Payment {

    @Override
    public PayState pay(String uid, double amount) {
        System.out.println("欢迎使用支付宝");
        System.out.println("查询账户余额，开始扣款");
        return new PayState(200, "支付成功", String.valueOf(amount));
    }
}
