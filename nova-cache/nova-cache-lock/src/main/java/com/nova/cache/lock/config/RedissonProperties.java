package com.nova.cache.lock.config;

import com.nova.cache.lock.enums.RedissonCons;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Redisson配置映射类，按照标准的spring-redis-data配置的
 * @author: wzh
 * @date: 2022/11/19 17:18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonProperties {

    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address;

    /**
     * 连接类型，支持single-单机节点，sentinel-哨兵，cluster-集群，masterSlave-主从
     */
    private RedissonCons.ConnectionType type;

    /**
     * redis连接密码
     */
    private String password;

    /**
     * 选取数据库
     */
    private int database;

}
