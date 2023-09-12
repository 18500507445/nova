package com.starter.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @description: redis自动配置类
 * @author: wzh
 * @date: 2023/4/22 20:08
 */
@Configuration
public class RedisAutoConfiguration {

    /**
     * 创建 RedisTemplate Bean，使用 JSON 序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置 RedisConnection 工厂。😈 它就是实现多种 Java Redis 客户端接入的秘密工厂。
        template.setConnectionFactory(factory);
        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 使用 JSON 序列化方式（库是 Jackson ），序列化 VALUE 。
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    /**
     * 第一数据源配置信息
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties primaryRedisProperties() {
        return new RedisProperties();
    }

    /**
     * 第二数据源配置信息
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis2")
    public RedisProperties secondRedisProperties() {
        return new RedisProperties();
    }

    @Bean(name = "redisService")
    public RedisService redisService() {
        return new RedisService();
    }

    @Value("${spring.redis2.lettuce.pool.max-idle:-1}")
    private int maxIdle;

    @Value("${spring.redis2.lettuce.pool.max-active:-1}")
    private int maxActive;

    @Value("${spring.redis2.lettuce.pool.max-wait:-1}")
    private long maxWaitMillis;

    @Value("${spring.redis2.lettuce.pool.min-idle:-1}")
    private int minIdle;

    @Value("${spring.redis2.timeout:-1}")
    private int timeout;

    @Bean(name = "secondRedisTemplate")
    public RedisTemplate<String, Object> secondRedisTemplate(@Value("${spring.redis2.database:-1}") int database,
                                                             @Value("${spring.redis2.host:default}") String hostName,
                                                             @Value("${spring.redis2.port:-1}") int port,
                                                             @Value("${spring.redis2.password:default}") String password,
                                                             @Qualifier("secondRedisProperties") RedisProperties secondRedisProperties) {
        if ("default".equals(hostName)) {
            return null;
        }
        System.out.println("secondRedisProperties = " + secondRedisProperties);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory(database, hostName, port, password));
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }

    /**
     * 使用lettuce配置Redis连接信息
     *
     * @param database Redis数据库编号
     * @param hostName 服务器地址
     * @param port     端口
     * @param password 密码
     */
    public RedisConnectionFactory connectionFactory(int database, String hostName, int port, String password) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
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
        genericObjectPoolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(configuration, clientConfig);
        lettuce.afterPropertiesSet();
        return lettuce;
    }
}
