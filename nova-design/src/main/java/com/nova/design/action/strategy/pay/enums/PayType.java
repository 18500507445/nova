package com.nova.design.action.strategy.pay.enums;


import com.nova.design.action.strategy.pay.payport.AliPay;
import com.nova.design.action.strategy.pay.payport.Payment;
import com.nova.design.action.strategy.pay.payport.WeChatPay;

/**
 * @description: 姑且把这个枚举当做一个常量去维护
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public enum PayType {

    ALI_PAY(new AliPay()),
    WECHAT_PAY(new WeChatPay());

    private Payment payment;

    PayType(Payment payment) {
        this.payment = payment;
    }

    public Payment get() {
        return this.payment;
    }
}
