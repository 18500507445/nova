package com.nova.mq.active;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Session;

/**
 * @description: ActiveMQConfig
 * @author: wangzehui
 * @date: 2022/8/4 21:29
 */
@Configuration
@EnableJms
public class ActiveMqConfig {

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,设置为10次
        redeliveryPolicy.setMaximumRedeliveries(10);
        //重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("failover:(tcp://ip:port,tcp://ip:port)?randomize=false&initialReconnectDelay=100&timeout=5000");
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy());
        return connectionFactory;
    }

    @Bean("jmsQueueTemplate")
    public JmsTemplate jmsQueueTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        //定义持久化后节点挂掉以后，重启可以继续消费.NON_PERSISTENT 表示非持久化，PERSISTENT 表示持久化
        jmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        jmsTemplate.setConnectionFactory(connectionFactory());
//        Session.AUTO_ACKNOWLEDGE  消息自动签收
//        Session.CLIENT_ACKNOWLEDGE  客户端调用acknowledge方法手动签收
//        Session.DUPS_OK_ACKNOWLEDGE 不必必须签收，消息可能会重复发送
        jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);//客户端签收模式
        return jmsTemplate;
    }

    @Bean("jmsListenerContainerTopic")
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic() {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setRecoveryInterval(1000L);
        bean.setConcurrency("1-3");
        // 创建连接的方法
        bean.setConnectionFactory(connectionFactory());
        return bean;
    }

    @Bean("jacksonJmsMessageConverter")
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager() {
        return new JmsTransactionManager(connectionFactory());
    }

}
