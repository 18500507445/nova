package com.nova.mq.rabbit.listener;

import cn.hutool.json.JSONUtil;
import com.nova.mq.rabbit.config.RabbitConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 广播模式，1条消息多个监听
 * @author: wzh
 * @date: 2023/1/6 21:36
 */
@Component
public class FanoutListener {

    /**
     * email广播
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_FANOUT_EMAIL))
    public void email(Message message) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("广播-email,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * sms广播
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_FANOUT_SMS))
    public void sms(Message message) {
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("广播-sms,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }
}
