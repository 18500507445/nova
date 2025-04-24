package com.nova.cache.lock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Redis连接方式
 * 包含:standalone-单节点部署方式、sentinel-哨兵部署方式、cluster-集群方式、masterSlave-主从部署方式
 *
 * @author: wzh
 * @date: 2022/12/26 23:10
 */
@Getter
@AllArgsConstructor
public enum RedisConnectionEnum {

    /**
     * 单节点部署方式
     */
    STANDALONE("standalone", "单节点部署方式"),

    /**
     * 哨兵部署方式
     */
    SENTINEL("sentinel", "哨兵部署方式"),

    /**
     * 集群部署方式
     */
    CLUSTER("cluster", "集群方式"),

    /**
     * 主从部署方式
     */
    MASTER_SLAVE("masterSlave", "主从部署方式");

    private final String connection_type;

    private final String connection_desc;
}