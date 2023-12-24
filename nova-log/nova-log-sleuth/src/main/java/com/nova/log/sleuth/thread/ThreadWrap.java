package com.nova.log.sleuth.thread;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author: wzh
 * @description 线程包装类
 * @date: 2023/12/24 22:36
 */
public class ThreadWrap {

    public static <T> Callable<T> callableWrap(final Callable<T> callable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (null != copyOfContextMap) {
                    MDC.setContextMap(copyOfContextMap);
                }
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable runnableWrap(final Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (null != copyOfContextMap) {
                    MDC.setContextMap(copyOfContextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
