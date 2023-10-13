package com.nova.shopping.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局常量枚举
 *
 * @author wzh
 * @date 2023/03/26 23:10
 */
@Getter
@AllArgsConstructor
public enum RedissonEnum {

    /**
     * Redis地址连接前缀
     */
    REDIS_CONNECTION_PREFIX("redis://", "Redis地址配置前缀");

    private final String constant_value;

    private final String constant_desc;
}

