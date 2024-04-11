package com.nova.cache.statemachine.manager.listener;

import com.nova.cache.statemachine.bean.Order;
import com.nova.cache.statemachine.enums.OrderState;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: wzh
 * @description: 状态监听
 * @date: 2024/04/11 09:39
 */
@Component
@WithStateMachine
@Transactional
public class OrderStatusListener {

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public boolean payListener(Message<Order> message) {
        Order order = (Order) message.getHeaders().get("order");
        if (order != null) {
            order.setOrderStatus(OrderState.WAIT_DELIVER);
        }
        System.err.println("支付，状态机反馈信息：" + message.getHeaders());
        return true;
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public boolean deliverListener(Message<Order> message) {
        Order order = (Order) message.getHeaders().get("order");
        if (order != null) {
            order.setOrderStatus(OrderState.WAIT_RECEIVE);
        }
        System.err.println("发货，状态机反馈信息：" + message.getHeaders());
        return true;
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public boolean receiveListener(Message<Order> message) {
        Order order = (Order) message.getHeaders().get("order");
        if (order != null) {
            order.setOrderStatus(OrderState.FINISH);
        }
        System.err.println("收货，状态机反馈信息：" + message.getHeaders());
        return true;
    }

}