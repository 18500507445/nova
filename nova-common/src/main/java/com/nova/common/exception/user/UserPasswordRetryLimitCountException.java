package com.nova.common.exception.user;

/**
 * 用户密码错误记数异常类
 */
public class UserPasswordRetryLimitCountException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitCountException(int retryLimitCount) {
        super("user.password.retry.limit.count", new Object[]{retryLimitCount});
    }
}
