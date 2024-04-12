package com.nova.cache.statemachine.config;

import com.nova.cache.statemachine.enums.OrderState;
import com.nova.cache.statemachine.enums.OrderStateChangeAction;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;

import java.time.Duration;
import java.util.EnumSet;

/**
 * @author: wzh
 * @description: 订单状态机
 * @date: 2024/04/11 09:39
 */
@Configuration
@EnableStateMachine
public class OrderStatusMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateChangeAction> {

    @Primary
    @Bean(name = "redisProperties")
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties primaryRedisProperties() {
        return new RedisProperties();
    }

    @Bean(name = "orderStateMachinePersister")
    public RedisStateMachinePersister<OrderState, OrderStateChangeAction> getRedisPersister(RedisProperties redisProperties) {
        RedisConnectionFactory redisConnectionFactory = getRedisconnectionFactory(redisProperties);
        RedisStateMachineContextRepository<OrderState, OrderStateChangeAction> repository = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist<OrderState, OrderStateChangeAction> p = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(p);
    }

    /**
     * 配置状态
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> states) throws Exception {
        states.withStates()
                .initial(OrderState.WAIT_PAYMENT)
                .states(EnumSet.allOf(OrderState.class));
    }

    /**
     * 配置状态转换事件关系
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderState.WAIT_PAYMENT).target(OrderState.WAIT_DELIVER).event(OrderStateChangeAction.PAYED)
                .and().withExternal()
                .source(OrderState.WAIT_DELIVER).target(OrderState.WAIT_RECEIVE).event(OrderStateChangeAction.DELIVERY)
                .and().withExternal()
                .source(OrderState.WAIT_RECEIVE).target(OrderState.FINISH).event(OrderStateChangeAction.RECEIVED);
    }

    /**
     * 通用获取连接工厂方法
     * 使用lettuce配置Redis连接信息
     */
    private static RedisConnectionFactory getRedisconnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        int database = redisProperties.getDatabase();

        Duration timeout = null == redisProperties.getTimeout() ? Duration.ZERO : redisProperties.getTimeout();

        int maxIdle = redisProperties.getLettuce().getPool().getMaxIdle();
        int minIdle = redisProperties.getLettuce().getPool().getMinIdle();
        int maxActive = redisProperties.getLettuce().getPool().getMaxActive();
        Duration maxWait = redisProperties.getLettuce().getPool().getMaxWait();

        configuration.setHostName(host);
        configuration.setPort(port);
        if (null != password) {
            configuration.setPassword(password);
        }
        if (database != 0) {
            configuration.setDatabase(database);
        }
        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWait(maxWait);

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(timeout)
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(configuration, clientConfig);
        lettuce.afterPropertiesSet();
        return lettuce;
    }

}