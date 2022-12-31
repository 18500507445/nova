package com.nova.design.action.strategy.pay.entity;


import com.nova.design.action.strategy.pay.enums.PayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 订单实体类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String uid;

    private String orderId;

    private double amount;

    public PayState pay(PayType payType) {
        return payType.get().pay(this.uid, this.amount);
    }
}
