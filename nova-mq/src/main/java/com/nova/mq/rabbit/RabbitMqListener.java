package com.nova.mq.rabbit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.map.MapUtil;
import com.nova.common.constant.Destination;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: 测试rabbit-mq监听器
 * @Author: wangzehui
 * @Date: 2022/9/11 09:25
 */
@Slf4j
public class RabbitMqListener {

    /**
     * 消息确认机制（ACK）
     * <p>
     * 消息确认模式有：
     * AcknowledgeMode.NONE：自动确认。
     * AcknowledgeMode.AUTO：根据情况确认。
     * AcknowledgeMode.MANUAL：手动确认。
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue(Destination.TEST_DESTINATION), concurrency = "1-3", ackMode = "MANUAL")
    public void testRabbitMqListener(Map<String, Object> msg, Message message, Channel channel) {
        TimeInterval timer = DateUtil.timer();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("testRabbitMqListener====>回调监听开始");
        try {
            String userId = MapUtil.getStr(msg, "userId");
            System.out.println(userId);
        } catch (Exception e) {
            log.error("testRabbitMqListener异常:{}" + e.getMessage());
        } finally {
            try {
                /**
                 * 确认消息，参数说明：long deliveryTag：唯一标识 ID。
                 * boolean multiple：是否批处理，当该参数为 true 时，则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
                 */
                channel.basicAck(deliveryTag, false);

                /**
                 * 否定消息，参数说明：
                 * long deliveryTag：唯一标识 ID。
                 * boolean multiple：是否批处理，当该参数为 true 时，
                 * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
                 * boolean requeue：如果 requeue 参数设置为 true，
                 * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
                 * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
                 * 而不会把它发送给新的消费者。
                 */
                //channel.basicNack(deliveryTag, true, false);

                /**
                 * 拒绝消息，参数说明：
                 * long deliveryTag：唯一标识 ID。
                 * boolean requeue：如果 requeue 参数设置为 true，
                 * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
                 * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
                 * 而不会把它发送给新的消费者。
                 */
                //channel.basicReject(deliveryTag, true);
            } catch (IOException e) {
                log.error("testRabbitMqListener====>消费异常");
            }
            log.info("testRabbitMqListener====>结束执行time:{}", timer.interval());
        }
    }
}
