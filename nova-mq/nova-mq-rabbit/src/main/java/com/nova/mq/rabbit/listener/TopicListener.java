package com.nova.mq.rabbit.listener;

import cn.hutool.json.JSONUtil;
import com.nova.mq.rabbit.config.RabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 主题模式
 * @author: wzh
 * @date: 2023/1/7 08:52
 */
@Component
public class TopicListener {

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_TOPIC_ONE)
    public void one(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("主题模式1,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_TOPIC_TWO)
    public void two(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("主题模式2,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_TOPIC_THREE)
    public void three(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("主题模式3-one,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 多个队列监听，自动多worker模式
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_TOPIC_THREE)
    public void threeCopy(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("主题模式3-two,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }
}
