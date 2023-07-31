package com.starter.mongo.utils;


import com.starter.mongo.exception.EasyMongoException;

/**
 * 异常工具
 *
 * @author loser
 * @date 2023/6/13
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static EasyMongoException mpe(String msg, Throwable t, Object... params) {
        return new EasyMongoException(StringUtils.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static EasyMongoException mpe(String msg, Object... params) {
        return new EasyMongoException(StringUtils.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t 异常
     * @return 返回异常
     */
    public static EasyMongoException mpe(Throwable t) {
        return new EasyMongoException(t);
    }

}
