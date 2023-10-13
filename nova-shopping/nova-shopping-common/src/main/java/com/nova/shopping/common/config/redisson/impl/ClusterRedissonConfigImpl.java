package com.nova.shopping.common.config.redisson.impl;

import com.nova.shopping.common.config.redisson.RedissonConfigStrategy;
import com.nova.shopping.common.config.redisson.RedissonProperties;
import com.nova.shopping.common.enums.RedissonEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.redisson.config.Config;

/**
 * 集群方式Redisson配置
 *
 * @author wzh
 * @date 2023/03/26 23:10
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
                if (StringUtils.isNotBlank(password)) {
                    config.useClusterServers().setPassword(password);
                }
            }
            log.info("初始化[cluster]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("cluster Redisson init error：{}", e.getMessage());
            e.printStackTrace();
        }
        return config;
    }
}