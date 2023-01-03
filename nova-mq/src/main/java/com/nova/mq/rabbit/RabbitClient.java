package com.nova.mq.rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/3 09:47
 */
public class RabbitClient {

    /**
     * 使用ConnectionFactory来创建连接
     */
    private final ConnectionFactory factory = new ConnectionFactory();

    public static final String HOST = "47.100.174.176";

    public static final int POST = 5672;

    public static final String USER_NAME = "root";

    public static final String PASSWORD = "@wangzehui123";

    public static final String VIRTUAL_HOST = "/";

    private static Connection connection;

    public RabbitClient() {

    }

    /**
     * 取实例的方法
     */
    public static RabbitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 私有静态内部类
     */
    private static class SingletonHolder {
        private static final RabbitClient INSTANCE = new RabbitClient();
    }

    public Connection getConnection() {
        //设定连接信息，基操
        factory.setHost(HOST);
        factory.setPort(POST);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);

        /**
         * 这里不使用try-with-resource，因为消费者是一直等待新的消息到来，
         * 然后按照我们设定的逻辑进行处理，所以这里不能在定义完成之后就关闭连接
         */
        try {
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
