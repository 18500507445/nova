package com.nova.book.design.structure.bridge.channel;


import com.nova.book.design.structure.bridge.mode.IPayMode;

/**
 * @description: 支付通道
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public abstract class Payment {

    protected IPayMode payMode;

    public Payment(IPayMode payMode) {
        this.payMode = payMode;
    }

    public abstract void transfer();

}
