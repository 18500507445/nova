package com.nova.shopping.common.config;

import com.nova.shopping.common.constant.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description: rabbitMq配置类
 * @author: wzh
 * @date: 2023/03/3 19:47
 */
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.addresses}")
    private String addresses;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.getRabbitConnectionFactory().setRequestedChannelMax(4095);
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(rabbitConnectionFactory());
    }

    /**
     * 直连交换机
     *
     * @return
     */
    @Bean("directExchange")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange(Constants.EXCHANGE_DIRECT).build();
    }

    /**
     * 死信交换机
     */
    @Bean("directDlExchange")
    public Exchange directDlExchange() {
        return ExchangeBuilder.directExchange(Constants.EXCHANGE_DIRECT_DLX).build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue queueDirectDl() {
        return new Queue(Constants.QUEUE_DIRECT_DLX);
    }

    /**
     * 死信队列-绑定死信交换机
     */
    @Bean
    public Binding bindingDirectDl() {
        return BindingBuilder.bind(queueDirectDl()).to(directDlExchange()).with("directDl").noargs();
    }

    /**
     * 异步下单队列
     */
    @Bean
    public Queue queueDirectCreateOrder() {
        return new Queue(Constants.QUEUE_DIRECT_CREATE_ORDER);
    }

    /**
     * 下单队列-绑定直连交换机
     */
    @Bean
    public Binding bindingDirectCreateOrder() {
        return BindingBuilder.bind(queueDirectCreateOrder()).to(directExchange()).with("createOrder").noargs();
    }

    /**
     * 查询订单队列
     * 指定死信队列、死信交换机、过期策略
     * todo 该队列不需要实现，30min后自动转到死信队列处理逻辑
     */
    @Bean
    public Queue queueDirectQueryOrder() {
        return QueueBuilder
                .nonDurable(Constants.QUEUE_DIRECT_QUERY_ORDER)
                .deadLetterExchange(Constants.EXCHANGE_DIRECT_DLX)
                .deadLetterRoutingKey("directDl")
                //如果30min处理，就自动删除
                .ttl(1000 * 60 * 30)
                .build();
    }

    /**
     * 查询队列-绑定直连交换机
     */
    @Bean
    public Binding bindingDirectQueryOrder() {
        return BindingBuilder.bind(queueDirectQueryOrder()).to(directExchange()).with("queryOrder").noargs();
    }

}
