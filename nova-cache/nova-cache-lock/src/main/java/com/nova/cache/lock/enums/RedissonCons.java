package com.nova.cache.lock.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wzh
 * @description: redission常量类
 * @date: 2025/04/24 17:10
 */
public class RedissonCons {

    //Redis地址连接前缀
    public static final String PREFIX = "redis://";

    /**
     * 锁类型
     */
    @Getter
    @AllArgsConstructor
    public enum LockType {

        /**
         * 可重入锁
         */
        REENTRANT,

        /**
         * 公平锁
         */
        FAIR,

        /**
         * 读锁
         */
        READ,

        /**
         * 写锁
         */
        WRITE
    }

    /**
     * Redis连接方式包含:single-单节点部署方式、sentinel-哨兵部署方式、cluster-集群方式、masterSlave-主从部署方式
     */
    @Getter
    @AllArgsConstructor
    public enum ConnectionType {

        SINGLE("single", "单节点"),

        SENTINEL("sentinel", "哨兵"),

        CLUSTER("cluster", "集群"),

        MASTER_SLAVE("masterSlave", "主从");

        private final String type;

        private final String description;

        public static ConnectionType getEnum(String type) {
            return EnumUtil.getBy(ConnectionType::getType, type, SINGLE);
        }
    }
}
