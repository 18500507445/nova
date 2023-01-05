package com.nova.mq.rabbit.config;

import com.nova.common.constant.RabbitConstants;
import org.springframework.amqp.core.*;
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
     * 定义交换机Bean，可以很多个
     *
     * @return
     */
    @Bean("directExchange")
    public Exchange exchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.EXCHANGE_DIRECT).build();
    }

    /**
     * 定义消息队列
     *
     * @return
     */
    @Bean(RabbitConstants.QUEUE_DEFAULT)
    public Queue queue() {
        return QueueBuilder
                .nonDurable(RabbitConstants.QUEUE_DEFAULT)
                .build();
    }

    @Bean("binding")
    public Binding binding(@Qualifier("directExchange") Exchange exchange,
                           @Qualifier(RabbitConstants.QUEUE_DEFAULT) Queue queue) {
        //将我们刚刚定义的交换机和队列进行绑定
        return BindingBuilder
                //绑定队列
                .bind(queue)
                //到交换机
                .to(exchange)
                //使用自定义的routingKey
                .with("routing-key")
                .noargs();
    }

    /**
     * 直接创建一个用于JSON转换的Bean
     */
    @Bean("jacksonConverter")
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

}
