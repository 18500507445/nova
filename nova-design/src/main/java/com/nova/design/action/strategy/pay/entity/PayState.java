package com.nova.design.action.strategy.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 支付完成以后的状态
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayState {

    private int code;

    private Object data;

    private String msg;

    @Override
    public String toString() {
        return ("支付状态：[" + code + "]," + msg + ",交易详情：" + data);
    }
}
