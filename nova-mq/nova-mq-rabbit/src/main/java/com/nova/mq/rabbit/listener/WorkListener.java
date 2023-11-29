package com.nova.mq.rabbit.listener;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.nova.mq.rabbit.config.RabbitConfig;
import com.nova.mq.rabbit.config.RabbitConstants;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * @description: 分发机制，分为轮询（每个人一样多）和公平分发（干完手动应答后再取一个，也就是能者多劳）
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
     * {@link RabbitConfig#rabbitListenerContainerFactory(ConnectionFactory)} ()}
     *
     * @param message
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_ONE), containerFactory = "rabbitListenerContainerFactory")
    public void pollingOne(Message message, Channel channel) {
        channel.basicQos(1);
        ThreadUtil.sleep(500);
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("工作轮询模式1,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 工作队列1-2
     * 轮询
     *
     * @param message
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_ONE), containerFactory = "rabbitListenerContainerFactory")
    public void pollingTwo(Message message, Channel channel) {
        channel.basicQos(1);
        ThreadUtil.sleep(1000);
        long tag = message.getMessageProperties().getDeliveryTag();
        System.err.println("工作轮询模式2,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
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
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_TWO), concurrency = "2", containerFactory = "rabbitListenerContainerFactory")
    public void pollingThread(Message message) {
        long tag = message.getMessageProperties().getDeliveryTag();
        ThreadUtil.sleep(100);
        System.err.println("工作模式开启多线程,消息id：" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 工作队列-公平
     * 改成手动应答模式就变成公平了，能者多劳
     * 消息确认机制（ACK）
     * AcknowledgeMode.NONE：自动确认
     * AcknowledgeMode.AUTO：根据情况确认
     * AcknowledgeMode.MANUAL：手动确认
     *
     * @param message
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_THREE), ackMode = "MANUAL")
    public void fairOne(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            ThreadUtil.sleep(500);
        } finally {
            /**
             * ack表示确认消息
             * 参数说明：long deliveryTag：唯一标识 ID。
             * boolean multiple：是否批处理，当该参数为 true 时，则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             */
            channel.basicAck(tag, false);

            /**
             * 否定消息
             * boolean requeue：如果 requeue 参数设置为 true，RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 反之设置为false，则 RabbitMQ 立即会还把消息从队列中移除，而不会把它发送给新的消费者。
             */
            //channel.basicNack(tag, false, true);

            /**
             * 拒绝消息
             * 如果 requeue 参数设置为 true，则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，而不会把它发送给新的消费者。
             */
            //channel.basicReject(tag, true);
            System.err.println("工作公平模式1,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
        }
    }

    /**
     * 工作队列2-2
     * 公平
     *
     * @param message
     */
    @SneakyThrows
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_WORK_THREE), ackMode = "MANUAL")
    public void fairTwo(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            ThreadUtil.sleep(1000);
        } finally {
            channel.basicAck(tag, false);
            System.err.println("工作公平模式2,消息id:" + tag + ",消息内容：" + JSONUtil.toJsonStr(new String(message.getBody())));
        }
    }


}
