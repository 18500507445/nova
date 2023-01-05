package com.nova.mq.rabbit;

import com.nova.common.constant.RabbitConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/3 09:52
 */
public class RabbitSimpleTest {

    public static final String QUEUE_NAME = RabbitConstants.QUEUE_DIRECT;

    @Test
    public void producer() {
        // 1: 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2: 设置连接属性
        connectionFactory.setHost("47.100.174.176");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("@wangzehui123");

        Connection connection;
        Channel channel;

        try {
            // 3: 从连接工厂中获取连接
            connection = connectionFactory.newConnection("测试");
            // 4: 从连接中获取通道channel
            channel = connection.createChannel();

            /**
             *  如果队列不存在，则会创建
             *  Rabbitmq不允许创建两个相同的队列名称，否则会报错。
             *
             *  @params1： queue 队列的名称
             *  @params2： durable 队列是否持久化
             *  @params3： exclusive 是否排他，即是否私有的，如果为true,会对当前队列加锁，其他的通道不能访问，并且连接自动关闭
             *  @params4： autoDelete 是否自动删除，当最后一个消费者断开连接之后是否自动删除消息。
             *  @params5： arguments 可以设置队列附加参数，设置队列的有效期，消息的最大长度，队列的消息生命周期等等。
             *
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);


            /**
             * 发送消息给中间件rabbitmq-server
             *
             * @params1: 交换机exchange
             * @params2: 队列名称/routing
             * @params3: 属性配置
             * @params4: 发送消息的内容
             */
            channel.basicPublish("", QUEUE_NAME, null, "Hello World!".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送消息出现异常...");
        }
    }


    @Test
    public void consumer() throws Exception {
        // 1: 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2: 设置连接属性
        connectionFactory.setHost("47.100.174.176");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("@wangzehui123");

        // 3: 从连接工厂中获取连接
        Connection connection = connectionFactory.newConnection("测试");
        // 4: 从连接中获取通道channel
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
