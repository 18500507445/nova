package com.nova.mq.rabbit.config;

import com.nova.common.constant.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
     * 直连交换机
     *
     * @return
     */
    @Bean("directExchange")
    public Exchange exchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.EXCHANGE_DIRECT).build();
    }

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean("directDlExchange")
    public Exchange dlExchange() {
        return ExchangeBuilder.directExchange("dlx.direct").build();
    }

    /**
     * 定义消息队列
     *
     * @return
     */
    @Bean(RabbitConstants.QUEUE_DIRECT)
    public Queue queue() {
        return QueueBuilder
                .nonDurable(RabbitConstants.QUEUE_DIRECT)
                .build();
    }

    @Bean("binding")
    public Binding binding(@Qualifier("directExchange") Exchange exchange,
                           @Qualifier(RabbitConstants.QUEUE_DIRECT) Queue queue) {
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
     * 死信队列
     *
     * @return
     */
    @Bean("yydsDlQueue")
    public Queue dlQueue() {
        return QueueBuilder
                .nonDurable("dl-yyds")
                .build();
    }

    /**
     * 死信交换机和死信队列进绑定
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean("dlBinding")
    public Binding dlBinding(@Qualifier("directDlExchange") Exchange exchange,
                             @Qualifier("yydsDlQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("dl-yyds")
                .noargs();
    }

    /**
     * 正常队列
     *
     * @return
     */
    @Bean("yydsQueue")
    public Queue yydsQueue() {
        return QueueBuilder
                .nonDurable("yyds")
                //指定死信交换机
                .deadLetterExchange("dlx.direct")
                //指定死信RoutingKey
                .deadLetterRoutingKey("dl-yyds")
                //如果5秒没处理，就自动删除
                .ttl(1000 * 5)
                .build();
    }

    @Bean("bindingYyds")
    public Binding bindingYyds(@Qualifier("directExchange") Exchange exchange,
                           @Qualifier("yydsQueue") Queue queue){
        //将我们刚刚定义的交换机和队列进行绑定
        return BindingBuilder
                .bind(queue)   //绑定队列
                .to(exchange)  //到交换机
                .with("my-yyds")   //使用自定义的routingKey
                .noargs();
    }


}
