package com.nova.book.design.structure.bridge.channel;


import com.nova.book.design.structure.bridge.mode.IPayMode;

/**
 * @description: 支付宝
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AliPay extends Payment {

    public AliPay(IPayMode payMode) {
        super(payMode);
    }

    @Override
    public void transfer() {
        System.out.println("*** 支付宝");
        this.payMode.security();
    }

}
