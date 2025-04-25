package com.nova.cache.lock.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.nova.cache.lock.enums.RedissonCons;
import com.nova.cache.lock.enums.RedissonCons.ConnectionType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description: redisson自动装配类
 * @date: 2023/11/02 15:14
 */
@Slf4j
@Configuration
public class RedissonAutoConfiguration {

    @Resource
    private RedissonProperties redissonProperties;

    @Bean(name = "redissonLock")
    public RedissonLock redissonLock() {
        return new RedissonLock();
    }

    /**
     * 创建并配置RedissonClient Bean。
     *
     * @return 配置好的RedissonClient实例
     */
    @Bean
    public RedissonClient redissonClient() {
        // 创建Redisson配置对象
        Config config = new Config();

        // ip + port
        String address = redissonProperties.getAddress();
        if (StrUtil.isBlank(address)) {
            log.error("初始化RedissonLock config失败 属性配置address是空");
        }
        String password = redissonProperties.getPassword();
        int database = redissonProperties.getDatabase();

        ConnectionType connectionType = redissonProperties.getType();
        String fullAddress = RedissonCons.PREFIX + address;

        // 配置单节点模式
        if (ConnectionType.SINGLE == connectionType) {
            config.useSingleServer().setAddress(fullAddress);
            config.useSingleServer().setDatabase(database);
            if (StrUtil.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
        }
        // 哨兵
        else if (ConnectionType.SENTINEL == connectionType) {
            String[] addArr = address.split(",");
            String sentinelAliasName = addArr[0];
            // 设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (StrUtil.isNotBlank(password)) {
                config.useSentinelServers().setPassword(password);
            }
            // 设置sentinel节点的服务IP和端口
            for (int i = 1; i < addArr.length; i++) {
                config.useSentinelServers().addSentinelAddress(RedissonCons.PREFIX + addArr[i]);
            }
        }
        // 集群
        else if (ConnectionType.CLUSTER == connectionType) {
            String[] addArr = address.split(",");
            // 设置cluster节点的服务IP和端口
            for (String add : addArr) {
                config.useClusterServers().addNodeAddress(RedissonCons.PREFIX + add);
                if (StrUtil.isNotBlank(password)) {
                    config.useClusterServers().setPassword(password);
                }
            }
        }
        // 主从
        else if (ConnectionType.MASTER_SLAVE == connectionType) {
            String[] addArr = address.split(",");
            String masterNodeAddr = addArr[0];
            // 设置主节点ip
            config.useMasterSlaveServers().setMasterAddress(masterNodeAddr);
            if (StrUtil.isNotBlank(password)) {
                config.useMasterSlaveServers().setPassword(password);
            }
            config.useMasterSlaveServers().setDatabase(database);
            // 设置从节点，移除第一个节点，默认第一个为主节点
            List<String> slaveList = new ArrayList<>();
            for (String add : addArr) {
                slaveList.add(RedissonCons.PREFIX + add);
            }
            slaveList.remove(0);
            config.useMasterSlaveServers().addSlaveAddress(Convert.toStrArray(slaveList));
        }

        log.info("初始化 {} 方式Config，address:{}", connectionType.getDescription(), fullAddress);

        return Redisson.create(config);
    }

}
