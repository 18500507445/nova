package com.nova.mq;

import com.nova.common.constant.Destination;
import com.nova.mq.rabbit.utlis.RabbitClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/3 09:52
 */
public class RabbitTest {

    public static final String QUEUE_NAME = Destination.RABBIT_QUEUE_DEFAULT;

    @Test
    public void send() throws IOException {
        Connection connection = RabbitClient.getInstance().getConnection();
        Channel channel = connection.createChannel();
        //声明队列，如果此队列不存在，会自动创建
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //将队列绑定到交换机
        channel.queueBind(QUEUE_NAME, "amq.direct", "routing-key");
        //发布新的消息，注意消息需要转换为byte[]
        channel.basicPublish("amq.direct", "routing-key", null, "Hello World!".getBytes());

    }

    /**
     * 消费消息
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitClient.getInstance().getConnection();
        Channel channel = connection.createChannel();

        //创建一个基本的消费者
        channel.basicConsume(QUEUE_NAME, false, (s, delivery) -> {

            System.out.println("msg = " + new String(delivery.getBody()));
            /**
             *  basicAck是确认应答，第一个参数是当前的消息标签，后面的参数是
             *  是否批量处理消息队列中所有的消息，如果为false表示只处理当前消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            /**
             *basicNack是拒绝应答，最后一个参数表示是否将当前消息放回队列，
             * 如果为false，那么消息就会被丢弃 ture扔回队列
             */
            //channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);

            /**
             * 跟上面一样，最后一个参数为false，只不过这里省了
             */
            //channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
        }, s -> {
        });
    }


}
