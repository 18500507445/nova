package com.nova.cache.statemachine.enums;

/**
 * @author: wzh
 * @description: 订单状态
 * @date: 2024/04/11 09:39
 */
public enum OrderState {
    // 待支付
    WAIT_PAYMENT,

    // 待发货
    WAIT_DELIVER,

    // 待收货
    WAIT_RECEIVE,

    // 完成
    FINISH;
}