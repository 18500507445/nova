package com.nova.lock.config.impl;

import com.nova.lock.enums.RedissonEnum;
import com.nova.lock.config.RedissonConfigStrategy;
import com.nova.lock.config.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * SentinelRedissonConfigImpl
 * 哨兵方式Redis连接配置
 *
 * @author wzh
 * @date 2022/12/26 23:10
 */
@Slf4j
public class SentinelRedissonConfigImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String sentinelAliasName = addrTokens[0];
            // 设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSentinelServers().setPassword(password);
            }
            // 设置sentinel节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                config.useSentinelServers().addSentinelAddress(RedissonEnum.REDIS_CONNECTION_PREFIX.getConstant_value() + addrTokens[i]);
            }
            log.info("初始化[sentinel]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("sentinel Redisson init error：{}", e.getMessage());
        }
        return config;
    }
}