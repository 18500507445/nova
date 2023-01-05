package com.nova.mq.rabbit.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 私信交换机监听
 * @author: wzh
 * @date: 2023/1/5 13:34
 */
@Component
public class DelayListener {

    //@RabbitHandler
    //@RabbitListener(queues = "yyds")
    //public void one(Message message) {
    //    System.out.println("正常消息队列监听器:=" + new String(message.getBody()));
    //}

    @RabbitHandler
    @RabbitListener(queues = "dl-yyds")
    public void oneTwo(Message message) {
        System.out.println("死信消息队列监听器:=" + new String(message.getBody()));
    }

}
