package com.nova.mq.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = "connectionFactory")
    public ConnectionFactory a() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.getRabbitConnectionFactory().setRequestedChannelMax(4095);
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    /**
     * 生产者设置
     * （1）添加消息转换器
     */
    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 监听者设置
     * （1）添加消息转换器MessageConverter
     * （2）每次取一条
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
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
     * 主题交换机
     */
    @Bean("topicExchange")
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(RabbitConstants.EXCHANGE_TOPIC).build();
    }

    /**
     * 简单队列1
     *
     * @return
     */
    @Bean
    public Queue queueSimpleOne() {
        // Queue:名字 默认 | durable: 是否持久化 | exclusive: 是否排它 | autoDelete: 是否自动删除
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
     * 将email队列-绑定到广播交换机
     *
     * @return
     */
    @Bean
    public Binding bindingFanoutEmail() {
        return BindingBuilder.bind(queueFanoutEmail()).to(fanoutExchange()).with("").noargs();
    }

    /**
     * 将sms队列-绑定到广播交换机
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
     * 直连队列1-绑定直连交换机
     */
    @Bean
    public Binding bindingDirectOne() {
        return BindingBuilder
                .bind(queueDirectOne())
                .to(directExchange())
                .with("directOne").noargs();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue queueDirectDl() {
        return new Queue(RabbitConstants.QUEUE_DIRECT_DLX);
    }

    /**
     * 死信队列-绑定死信交换机
     */
    @Bean
    public Binding bindingDirectDl() {
        return BindingBuilder
                .bind(queueDirectDl())
                .to(directDlExchange())
                .with("directDl").noargs();
    }

    /**
     * 直连队列2-指定死信队列、死信交换机、策略
     */
    @Bean
    public Queue queueDirectTwo() {
        return QueueBuilder
                .nonDurable(RabbitConstants.QUEUE_DIRECT_TWO)
                //指定死信交换机
                .deadLetterExchange(RabbitConstants.EXCHANGE_DIRECT_DLX)
                //指定死信RoutingKey
                .deadLetterRoutingKey("directDl")
                .build();
    }

    /**
     * 直连队列2-绑定直连交换机
     */
    @Bean
    public Binding bindingDirectTwo() {
        return BindingBuilder.bind(queueDirectTwo()).to(directExchange()).with("directTwo").noargs();
    }

    /**
     * 直连队列3-指定死信队列、死信交换机、过期策略
     */
    @Bean
    public Queue queueDirectThree() {
        return QueueBuilder
                .nonDurable(RabbitConstants.QUEUE_DIRECT_THREE)
                .deadLetterExchange(RabbitConstants.EXCHANGE_DIRECT_DLX)
                .deadLetterRoutingKey("directDl")
                //如果5秒没处理，就自动删除
                .ttl(1000 * 5)
                .build();
    }

    /**
     * 直连队列3-绑定直连交换机
     */
    @Bean
    public Binding bindingDirectThree() {
        return BindingBuilder.bind(queueDirectThree()).to(directExchange()).with("directThree").noargs();
    }

    /**
     * 主题队列1
     */
    @Bean
    public Queue queueTopicOne() {
        return new Queue(RabbitConstants.QUEUE_TOPIC_ONE);
    }

    /**
     * 主题队列2
     */
    @Bean
    public Queue queueTopicTwo() {
        return new Queue(RabbitConstants.QUEUE_TOPIC_TWO);
    }

    /**
     * 主题队列3
     */
    @Bean
    public Queue queueTopicThree() {
        return new Queue(RabbitConstants.QUEUE_TOPIC_THREE);
    }

    /**
     * 绑定主题交换机1
     * <p>
     * ● * - 表示任意的一个单词
     * ● # - 表示0个或多个单词
     */
    @Bean
    public Binding bindingTopicOne() {
        return BindingBuilder.bind(queueTopicOne()).to(topicExchange()).with("#.one.#").noargs();
    }

    /**
     * 绑定主题交换机2
     * routingKey：three.one.two,只会1、3接收
     */
    @Bean
    public Binding bindingTopicTwo() {
        return BindingBuilder.bind(queueTopicTwo()).to(topicExchange()).with("*.two.#").noargs();
    }

    /**
     * 绑定主题交换机3
     * routingKey：one.three,只会1接收
     */
    @Bean
    public Binding bindingTopicThree() {
        return BindingBuilder.bind(queueTopicThree()).to(topicExchange()).with("three.#").noargs();
    }
}
