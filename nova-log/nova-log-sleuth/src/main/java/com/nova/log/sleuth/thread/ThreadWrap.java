package com.nova.log.sleuth.thread;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author: wzh
 * @description: 线程包装类，主子线程MDC传递
 * @date: 2023/12/24 22:36
 */
@Slf4j
public class ThreadWrap {

    public static <T> Callable<T> callableWrap(final Callable<T> callable) {
        // 获取当前线程的MDC上下文信息的副本
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

        // 返回一个新的Callable 对象
        return () -> {
            try {
                // 如果副本不为null，则将副本的上下文信息设置到当前线程的MDC中
                // todo 这里的当前线程是指在调用call()方法时的线程和try外面的不是一个线程哦
                if (null != copyOfContextMap) {
                    MDC.setContextMap(copyOfContextMap);
                }
                // 调用原始的Callable对象的call() 方法，并返回结果
                return callable.call();
            } finally {
                // 在finally块中清除当前线程的MDC上下文信息
                MDC.clear();
            }
        };
    }

    public static Runnable runnableWrap(final Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        log.warn("main-threadId = {}", Thread.currentThread().getId());
        return () -> {
            try {
                if (null != copyOfContextMap) {
                    MDC.setContextMap(copyOfContextMap);
                }
                log.warn("sub-threadId = {}", Thread.currentThread().getId());
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
