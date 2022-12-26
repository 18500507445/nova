package com.nova.limit.config.strategy;

import com.nova.limit.common.GlobalConstant;
import com.nova.limit.config.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * StandaloneRedissonConfigStrategyImpl
 * 单机方式Redisson配置
 *
 * @author wangzehui
 * @date 2022/12/26 23:10
 */
@Slf4j
public class StandaloneRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化[standalone]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("standalone Redisson init error：{}", e.getMessage());
        }
        return config;
    }
}