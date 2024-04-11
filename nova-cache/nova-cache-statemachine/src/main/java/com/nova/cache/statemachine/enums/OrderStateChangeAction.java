package com.nova.cache.statemachine.enums;

/**
 * @author: wzh
 * @description: 订单状态转换行为
 * @date: 2024/04/11 09:39
 */
public enum OrderStateChangeAction {

    //支付
    PAYED,

    //发货
    DELIVERY,

    //收货
    RECEIVED;
}