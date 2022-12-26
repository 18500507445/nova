package com.nova.limit.common;

/**
 * 分布式锁异常类
 *
 * @author wangzehui
 * @date 2022/01/18 17:53
 */
public class DistributeLockException extends RuntimeException {

    public DistributeLockException(String msg) {
        super(msg);
    }

    public DistributeLockException(Throwable cause) {
        super(cause);
    }
}
