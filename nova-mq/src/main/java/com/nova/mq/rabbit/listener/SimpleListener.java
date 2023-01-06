package com.nova.mq.rabbit.listener;

import cn.hutool.json.JSONUtil;
import com.nova.common.constant.RabbitConstants;
import com.nova.common.core.model.business.MessageBO;
import com.nova.mq.rabbit.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 简单模式监听器
 * @author: wzh
 * @date: 2023/1/6 15:42
 */
@Component
public class SimpleListener {

    /**
     * 简单队列1 (无返回值)
     * RabbitListener(queues = RabbitConstants.QUEUE_SIMPLE_ONE) 用来绑定队列
     * {@link RabbitConfig#queueSimpleOne()} 需要申明队列
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_SIMPLE_ONE)
    public void one(Message message) {
        System.out.println("简单模式one消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 简单队列2 (有返回值)
     *
     * @param message
     * @return
     */
    @RabbitHandler
    @RabbitListener(queues = RabbitConstants.QUEUE_SIMPLE_TWO)
    public String two(Message message) {
        System.out.println("简单模式two消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
        return "收到";
    }

    /**
     * 简单队列3 (无序申明队列，直接注解搞定)
     * queuesToDeclare属性：如果没有就创建队列 new Queue，否则不创建，和@Queue搭配使用
     *
     * @param message
     * @return
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_THREE))
    public void three(Message message) {
        System.out.println("简单模式three消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 简单队列4 实体类接收参数
     *
     * @param message
     * @return
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_FOUR))
    public void four(MessageBO message) {
        System.out.println("简单模式four消息：" + JSONUtil.toJsonStr(message));
    }

    /**
     * 简单队列5 map接收参数
     *
     * @param msg
     * @return
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_FIVE))
    public void five(Map<String, Object> msg) {
        System.out.println("简单模式five消息：" + JSONUtil.toJsonStr(msg));
    }

    /**
     * 简单队列6 多线程创建队列处理消息
     * concurrency = "2" 2个线程
     * concurrency = "1-3"，表示并发数，表示有多少个消费者处理队列里的消息 最小-最大数 1-3个线程
     * <p>
     * https://img-blog.csdnimg.cn/5f4117cc52ea4f8888c6894803f1a1d6.jpeg#pic_center
     */
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_SIX), concurrency = "2")
    public void six(Message message) {
        System.out.println("简单模式six消息：" + JSONUtil.toJsonStr(new String(message.getBody())));
    }

    /**
     * 简单队列7 消息确认机制（ACK）
     * <p>
     * 消息确认模式有：
     * AcknowledgeMode.NONE：自动确认
     * AcknowledgeMode.AUTO：根据情况确认
     * AcknowledgeMode.MANUAL：手动确认
     */
    @SneakyThrows
    @RabbitListener(queuesToDeclare = @Queue(RabbitConstants.QUEUE_SIMPLE_SEVEN), ackMode = "MANUAL")
    public void seven(Message message, Channel channel) {
        long tag = message.getMessageProperties().getDeliveryTag();
        String body = null;
        try {
            body = new String(message.getBody());
            System.out.println("简单模式seven消息：" + body);
        } finally {
            /**
             * ack表示确认消息，参数说明：long deliveryTag：唯一标识 ID。
             * boolean multiple：是否批处理，当该参数为 true 时，则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             */
            channel.basicAck(tag, false);

            /**
             * 否定消息，参数说明：
             * boolean multiple：是否批处理，当该参数为 true 时，则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             * boolean requeue：如果 requeue 参数设置为 true，
             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
             * 而不会把它发送给新的消费者。
             */
            //channel.basicNack(deliveryTag, true, false);

            /**
             * 拒绝消息，参数说明：
             * boolean requeue：如果 requeue 参数设置为 true，
             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
             * 而不会把它发送给新的消费者。
             */
            //channel.basicReject(deliveryTag, true);
        }
    }

}
