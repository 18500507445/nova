package com.nova.cache.statemachine.bean;

import com.nova.cache.statemachine.enums.OrderState;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: wzh
 * @description: 模拟订单类
 * @date: 2024/04/11 09:39
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private OrderState orderStatus;
}