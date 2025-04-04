package com.nova.mq.rabbit;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.business.MessageBO;
import com.nova.mq.rabbit.config.RabbitConstants;
import com.nova.mq.rabbit.listener.DirectSimpleListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/6 15:44
 */
@SpringBootTest
public class RabbitMqTest {

    public static final String QUEUE_NAME = "queue-default";

    public static final String MSG = "Hello World!";

    /**
     * RabbitTemplate为我们封装了大量的RabbitMQ操作，已经由Starter提供，因此直接注入使用即可
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产者
     */
    @Test
    public void producer() {
        // 1: 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2: 设置连接属性
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("123456");

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
            System.err.println("发送消息出现异常...");
        }
    }

    /**
     * 消费者
     *
     * @throws Exception
     */
    @Test
    public void consumer() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("123456");
        Connection connection = connectionFactory.newConnection("测试");
        Channel channel = connection.createChannel();
        //创建一个基本的消费者
        channel.basicConsume(QUEUE_NAME, false, (s, delivery) -> {
            System.err.println("msg = " + new String(delivery.getBody()));
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
             * 跟上面一样也是拒绝应答，最后一个参数为false，只不过这里省了
             */
            //channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
        }, s -> {
        });
    }

    //--------------简单直连模式，使用默认的系统直连交换机，不用config进行绑定，路由key等于队列名称--------------

    /**
     * 简单模式1
     * {@link DirectSimpleListener#one(Message)}
     */
    @Test
    public void simpleOne() {
        rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_SIMPLE_ONE, MSG);
    }

    /**
     * 简单模式2，接收返回
     * {@link DirectSimpleListener#two(Message)}
     */
    @Test
    public void simpleTwo() {
        Object receive = rabbitTemplate.convertSendAndReceive(RabbitConstants.QUEUE_SIMPLE_TWO, MSG);
        System.err.println("receive = " + receive);
        ThreadUtil.sleep(5000);
    }

    /**
     * 简单模式3
     * {@link DirectSimpleListener#three(Message)}
     */
    @Test
    public void simpleThree() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", 1);
        map.put("message", MSG);
        rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_SIMPLE_THREE, map);
    }

    /**
     * 简单模式4
     * {@link DirectSimpleListener#four(MessageBO)}
     */
    @Test
    public void simpleFour() {
        MessageBO build = MessageBO.builder().id(1).message(MSG).build();
        rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_SIMPLE_FOUR, build);
        ThreadUtil.sleep(5000);
    }

    /**
     * 简单模式5
     * {@link DirectSimpleListener#five(Map)}
     */
    @Test
    public void simpleFive() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", 1);
        map.put("message", MSG);
        rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_SIMPLE_FIVE, JSONUtil.toJsonStr(map));
        ThreadUtil.sleep(5000);
    }

    //--------------工作轮训模式，使用默认的系统直连交换机，不用config进行绑定，路由key等于队列名称--------------

    /**
     * 工作轮询模式-轮询
     */
    @Test
    public void workPolling() {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_WORK_ONE, MSG);
        }
    }

    /**
     * 工作轮询模式-线程
     */
    @Test
    public void workThread() {
        for (int i = 0; i < 10000; i++) {
            rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_WORK_TWO, MSG);
        }
        ThreadUtil.sleep(50000);
    }

    /**
     * 工作轮询模式-公平
     */
    @Test
    public void workFair() {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_WORK_THREE, MSG);
        }
    }

    //--------------广播交换机，手动创建，config进行队列绑定，设置routingKey，优点：1对多，延迟适中--------------


    /**
     * 广播模式
     */
    @Test
    public void fanoutTest() {
        String exchangeName = RabbitConstants.EXCHANGE_FANOUT;
        rabbitTemplate.convertAndSend(exchangeName, "", MSG);
    }


    //--------------直连交换机，手动创建，config进行队列绑定，设置routingKey，优点：延迟低，速度快--------------


    /**
     * 直连模式1
     */
    @Test
    public void directTestOne() {
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_DIRECT, "directOne", MSG);
    }

    /**
     * 直连模式2
     * 拒绝进入死信队列
     */
    @Test
    public void directTestDlx() {
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_DIRECT, "directTwo", MSG);
    }

    /**
     * 直连模式3
     * 过期进入死信队列
     * todo 偷懒了，干脆队列三的监听都直接不写了，反正也不用他干活，设置了过期直接进入死信队列，哈哈
     */
    @Test
    public void directTestThree() {
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_DIRECT, "directThree", MSG);
    }

    //--------------主题交换机，手动创建，config进行队列绑定，设置routingKey，优点：模糊匹配，延迟适中--------------

    /**
     * 主题模式
     */
    @Test
    public void topicTest() {
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_TOPIC, "three.two.one", MSG);

        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_TOPIC, "three", MSG);
        }
    }

}
