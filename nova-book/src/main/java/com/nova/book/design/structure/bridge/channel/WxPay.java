package com.nova.book.design.structure.bridge.channel;


import com.nova.book.design.structure.bridge.mode.IPayMode;

/**
 * @description: 微信
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class WxPay extends Payment {

    public WxPay(IPayMode payMode) {
        super(payMode);
    }

    @Override
    public void transfer() {
        System.err.println("*** 微信");
        this.payMode.security();
    }

}
