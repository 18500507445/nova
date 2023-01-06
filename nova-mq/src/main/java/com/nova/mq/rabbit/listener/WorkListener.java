package com.nova.mq.rabbit.listener;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.constant.RabbitConstants;
import com.nova.mq.rabbit.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @description: 工作队列模式，分为轮询和公平分发(能者多劳)
 * @author: wzh
 * @date: 2023/1/6 17:54
 */
@Component
public class WorkListener {

    /**
     * 工作队列1-1
     * 轮询
     * <p>
     * 预获取消息数量 250
     * https://img-blog.csdnimg.cn/3540429e842142e8bb80eeb28809761a.jpeg#pic_center
     * <p>
     * (1)可以在配置类里添加listenerContainer设置PrefetchCount为1个
     * 然后RabbitListener注解添加containerFactory = "listenerContainer"
     * (2)channel.basicQos(1);
     * {@link RabbitConfig#listenerContainer()}
     *
     * @param message
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_ONE))
    public void pollingOne(Message message, Channel channel) {
        channel.basicQos(1);
        long tag = message.getMessageProperties().getDeliveryTag();
        System.out.println("工作轮询模式1,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 工作队列1-2
     * 轮询
     *
     * @param message
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_ONE))
    public void pollingTwo(Message message, Channel channel) {
        channel.basicQos(1);
        long tag = message.getMessageProperties().getDeliveryTag();
        System.out.println("工作轮询模式2,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 工作队列-多线程
     * concurrency = "2" 2个线程
     * concurrency = "1-3"，表示并发数，表示有多少个消费者处理队列里的消息 最小-最大数 1-3个线程
     * <p>
     * https://img-blog.csdnimg.cn/5f4117cc52ea4f8888c6894803f1a1d6.jpeg#pic_center
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_TWO), concurrency = "2", containerFactory = "listenerContainer")
    public void pollingThread(Message message) {
        long tag = message.getMessageProperties().getDeliveryTag();
        ThreadUtil.sleep(100);
        System.out.println("工作模式开启多线程,消息id：" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 工作队列2-1
     * 公平
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_THREE))
    public void three(Message message) {
        System.out.println("工作轮询模式2-1消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 工作队列2-2
     * 公平
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_THREE))
    public void four(Message message) {
        System.out.println("工作轮询模式2-2消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }


}
