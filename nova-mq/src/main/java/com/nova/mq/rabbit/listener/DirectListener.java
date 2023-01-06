package com.nova.mq.rabbit.listener;

import cn.hutool.json.JSONUtil;
import com.nova.common.constant.RabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 直连交换机
 * @author: wzh
 * @date: 2023/1/5 11:00
 */
@Component
public class DirectListener {

    /**
     * 直连模式1
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_DIRECT_ONE))
    public void one(Message message) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.out.println("直连模式one,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 直连模式2
     *
     * @param message
     * @param channel
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_DIRECT_TWO, ackMode = "MANUAL")
    public void two(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        //false消息从队列中移除
        channel.basicReject(tag, false);
        System.out.println("直连模式two,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 消息变成死信，可能是由于以下的原因：
     * 消息被拒绝
     * 消息过期
     * 队列达到最大长度
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_DIRECT_DLX)
    public void delay(Message message) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.out.println("死信监听,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }
}
