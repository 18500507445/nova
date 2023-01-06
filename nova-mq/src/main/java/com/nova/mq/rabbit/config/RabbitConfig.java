package com.nova.mq.rabbit.config;

import com.nova.common.constant.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description: rabbitMq配置类
 * @author: wzh
 * @date: 2022/9/3 19:47
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

    @Bean(name = "listenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory());
        //将PrefetchCount设定为1表示一次只能取一个
        factory.setPrefetchCount(1);
        return factory;
    }

    /**
     * 直连交换机
     *
     * @return
     */
    @Bean("directExchange")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.EXCHANGE_DIRECT).build();
    }

    /**
     * 广播交换机
     *
     * @return
     */
    @Bean("fanoutExchange")
    public Exchange fanoutExchange() {
        //默认持久化，不自动删除
        return ExchangeBuilder.fanoutExchange(RabbitConstants.EXCHANGE_FANOUT).build();
    }

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean("directDlExchange")
    public Exchange directDlExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.EXCHANGE_DIRECT_DLX).build();
    }

    /**
     * 简单队列1
     *
     * @return
     */
    @Bean
    public Queue queueSimpleOne() {
        return new Queue(RabbitConstants.QUEUE_SIMPLE_ONE);
    }

    /**
     * 简单队列2
     *
     * @return
     */
    @Bean
    public Queue queueSimpleTwo() {
        return new Queue(RabbitConstants.QUEUE_SIMPLE_TWO);
    }

    /**
     * 广播队列-email
     */
    @Bean
    public Queue queueFanoutEmail() {
        return new Queue(RabbitConstants.QUEUE_FANOUT_EMAIL);
    }

    /**
     * 广播队列-sms
     */
    @Bean
    public Queue queueFanoutSms() {
        return new Queue(RabbitConstants.QUEUE_FANOUT_SMS);
    }

    /**
     * 将email队列绑定到广播交换机
     *
     * @return
     */
    @Bean
    public Binding bindingFanoutEmail() {
        return BindingBuilder.bind(queueFanoutEmail()).to(fanoutExchange()).with("").noargs();
    }

    /**
     * 将sms队列绑定到广播交换机
     *
     * @return
     */
    @Bean
    public Binding bindingFanoutSms() {
        return BindingBuilder.bind(queueFanoutSms()).to(fanoutExchange()).with("").noargs();
    }

    /**
     * 直连队列1
     */
    @Bean
    public Queue queueDirectOne() {
        return new Queue(RabbitConstants.QUEUE_DIRECT_ONE);
    }

    /**
     * 绑定直连交换机1
     */
    @Bean
    public Binding bindingDirectOne() {
        return BindingBuilder.bind(queueDirectOne()).to(directExchange()).with("directOne").noargs();
    }


    /**
     * 死信队列
     */
    @Bean
    public Queue queueDirectDl() {
        return new Queue(RabbitConstants.QUEUE_DIRECT_DLX);
    }

    /**
     * 绑定直连死信交换机
     */
    @Bean
    public Binding bindingDirectDl() {
        return BindingBuilder.bind(queueDirectDl()).to(directExchange()).with("directDl").noargs();
    }

    /**
     * 直连队列2-指定死信队列和交换机
     */
    @Bean
    public Queue queueDirectTwo() {
        return QueueBuilder
                .nonDurable(RabbitConstants.QUEUE_DIRECT_TWO)
                //指定死信交换机
                .deadLetterExchange(RabbitConstants.EXCHANGE_DIRECT_DLX)
                //指定死信RoutingKey
                .deadLetterRoutingKey("directDl")
                //如果5秒没处理，就自动删除
                .ttl(1000 * 5)
                .build();
    }

    /**
     * 绑定直连交换机2
     */
    @Bean
    public Binding bindingDirectTwo() {
        return BindingBuilder.bind(queueDirectTwo()).to(directExchange()).with("directTwo").noargs();
    }

}
