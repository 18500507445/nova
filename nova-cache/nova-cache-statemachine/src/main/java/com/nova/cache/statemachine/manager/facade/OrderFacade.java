package com.nova.cache.statemachine.manager.facade;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.nova.cache.statemachine.bean.Order;
import com.nova.cache.statemachine.enums.OrderState;
import com.nova.cache.statemachine.enums.OrderStateChangeAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author: wzh
 * @description: 订单门面类
 * @date: 2024/04/11 14:59
 */
@Component
@RequiredArgsConstructor
public class OrderFacade {

    //注入Spring状态机，与状态机进行交互
    private final StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    //注入Spring状态机的RedisPersister存取工具，持久化状态机
    private final StateMachinePersister<OrderState, OrderStateChangeAction, String> orderStateMachinePersister;

    @Getter
    private final Map<Long, Order> orders = Maps.newConcurrentMap();

    public Order create(Long id) {
        Order order = new Order();
        order.setOrderStatus(OrderState.WAIT_PAYMENT);
        order.setOrderId(id);
        orders.put(order.getOrderId(), order);
        System.err.println("订单创建:" + JSONUtil.toJsonStr(order));
        return order;
    }

    public Order pay(long id) {
        Order order = orders.get(id);
        System.err.println("尝试支付，订单号：" + id);
        Message<OrderStateChangeAction> message = MessageBuilder.withPayload(OrderStateChangeAction.PAYED).
                setHeader("order", order).build();
        if (!sendEvent(message)) {
            System.err.println("支付失败, 状态异常，订单号：" + id);
        }
        return orders.get(id);
    }

    public Order deliver(long id) {
        Order order = orders.get(id);
        System.err.println("尝试发货，订单号：" + id);
        if (!sendEvent(MessageBuilder.withPayload(OrderStateChangeAction.DELIVERY)
                .setHeader("order", order).build())) {
            System.err.println("发货失败，状态异常，订单号：" + id);
        }
        return orders.get(id);
    }

    public Order receive(long id) {
        Order order = orders.get(id);
        System.err.println("尝试收货，订单号：" + id);
        if (!sendEvent(MessageBuilder.withPayload(OrderStateChangeAction.RECEIVED)
                .setHeader("order", order).build())) {
            System.err.println("收货失败，状态异常，订单号：" + id);
        }
        return orders.get(id);
    }

    /**
     * 发送状态转换事件
     *
     * @param message 消息
     */
    private synchronized boolean sendEvent(Message<OrderStateChangeAction> message) {
        boolean result = false;
        if (Objects.nonNull(message)) {
            Order order = (Order) message.getHeaders().get("order");
            if (Objects.nonNull(order)) {
                try {
                    //启动状态机
                    orderStateMachine.start();

                    //从Redis缓存中读取状态机，缓存的Key为orderId+"STATE"，这是自定义的，读者可以根据自己喜好定义
                    orderStateMachinePersister.restore(orderStateMachine, "ORDER_STATE_" + order.getOrderId());

                    result = orderStateMachine.sendEvent(message);

                    //将更改完订单状态的 状态机 存储到 Redis缓存
                    orderStateMachinePersister.persist(orderStateMachine, "ORDER_STATE_" + order.getOrderId());
                } catch (Exception e) {
                    System.err.println("e = " + e);
                } finally {
                    if (Objects.equals(order.getOrderStatus(), OrderState.FINISH)) {
                        orderStateMachine.stop();
                    }
                }
            }
        }
        return result;
    }
}
