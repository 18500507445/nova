package com.nova.mq.rabbit.listener;

import com.nova.common.constant.RabbitConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/5 11:00
 */
@Component
public class RabbitMqListenerOne {

    /**
     * 定义此方法为队列rabbit-default的监听器，一旦监听到新的消息，就会接受并处理
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_DEFAULT)
    public void one(Message message) {
        System.out.println("一号消息队列监听器:=" + new String(message.getBody()));
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_DEFAULT)
    public String two(Message message) {
        System.out.println("一号消息队列监听器:=" + new String(message.getBody()));
        return "收到";
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_DEFAULT)
    public String three(Message message) {
        System.out.println("一号消息队列监听器:=" + new String(message.getBody()));
        return "收到";
    }

}
