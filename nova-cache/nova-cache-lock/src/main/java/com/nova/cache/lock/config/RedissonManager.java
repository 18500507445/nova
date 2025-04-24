package com.nova.cache.lock.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.cache.lock.config.impl.ClusterRedissonConfigImpl;
import com.nova.cache.lock.config.impl.MasterSlaveRedissonConfigImpl;
import com.nova.cache.lock.config.impl.SentinelRedissonConfigImpl;
import com.nova.cache.lock.config.impl.StandaloneRedissonConfigImpl;
import com.nova.cache.lock.enums.RedisConnectionEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * RedissonManager
 * Redisson核心配置，用于提供初始化的redisson实例
 *
 * @author wzh
 * @date 2022/12/26 23:10
 */
@Getter
@Slf4j
public class RedissonManager {

    private Redisson redisson = null;

    private RedissonManager() {

    }

    public RedissonManager(RedissonProperties redissonProperties) {
        try {
            Config config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);
            redisson = (Redisson) Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson init error", e);
            throw new IllegalArgumentException("please input correct configurations," + "connectionType must in standalone/sentinel/cluster/masterslave");
        }
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedissonConfigFactory {

        private RedissonConfigFactory() {

        }

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if (factory == null) {
                synchronized (Object.class) {
                    if (factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }
            return factory;
        }

        /**
         * 根据连接类型获取对应连接方式的配置,基于策略模式
         *
         * @param redissonProperties redisson配置
         * @return Config
         */
        Config createConfig(RedissonProperties redissonProperties) {
            if (ObjectUtil.isNull(redissonProperties)) {
                return null;
            }
            if (StrUtil.isBlank(redissonProperties.getAddress())) {
                log.info("redisson.lock.server.address cannot be NULL!");
            }
            if (StrUtil.isBlank(redissonProperties.getType())) {
                log.info("redisson.lock.server.password cannot be NULL");
            }
            String connectionType = redissonProperties.getType();
            // 声明配置上下文
            RedissonConfigContext redissonConfigContext;
            if (connectionType.equals(RedisConnectionEnum.STANDALONE.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new StandaloneRedissonConfigImpl());
            } else if (connectionType.equals(RedisConnectionEnum.SENTINEL.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new SentinelRedissonConfigImpl());
            } else if (connectionType.equals(RedisConnectionEnum.CLUSTER.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new ClusterRedissonConfigImpl());
            } else if (connectionType.equals(RedisConnectionEnum.MASTER_SLAVE.getConnection_type())) {
                redissonConfigContext = new RedissonConfigContext(new MasterSlaveRedissonConfigImpl());
            } else {
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigContext.createRedissonConfig(redissonProperties);
        }
    }


}
