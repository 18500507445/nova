package com.nova.cache.lock.config.impl;

import cn.hutool.core.util.StrUtil;
import com.nova.cache.lock.config.RedissonConfigStrategy;
import com.nova.cache.lock.config.RedissonProperties;
import com.nova.cache.lock.enums.RedissonEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;

/**
 * 集群方式Redisson配置
 *
 * @author:wzh
 * @date:2022/12/26 23:10
 */
@Slf4j
public class ClusterRedissonConfigImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            String[] addrTokens = address.split(",");
            // 设置cluster节点的服务IP和端口
            for (String addrToken : addrTokens) {
                config.useClusterServers().addNodeAddress(RedissonEnum.REDIS_CONNECTION_PREFIX.getConstant_value() + addrToken);
                if (StrUtil.isNotBlank(password)) {
                    config.useClusterServers().setPassword(password);
                }
            }
            log.info("初始化[cluster]方式Config,redisAddress:{}", address);
        } catch (Exception e) {
            log.error("cluster Redisson init error：{}", e.getMessage());
        }
        return config;
    }
}