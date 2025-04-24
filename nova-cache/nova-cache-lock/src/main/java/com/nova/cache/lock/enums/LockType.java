package com.nova.cache.lock.enums;

/**
 * 锁类型
 *
 * @author:wzh
 * @date:2023/09/25 05:10
 */
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
