package com.nova.common.utils.thread;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author: wzh
 * @description 自定义线程池，包装DMC
 * @date: 2023/12/10 16:28
 */
public class ThreadPoolTaskExecutorMdcUtil extends ThreadPoolTaskExecutor {

    @Override
    public void execute(@NotNull Runnable task) {
        super.execute(wrap(task));
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return super.submit(wrap(task));
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return super.submit(wrap(task));
    }

    private <T> Callable<T> wrap(final Callable<T> callable) {
        // 获取当前线程的MDC上下文信息
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            if (context != null) {
                // 传递给子线程
                MDC.setContextMap(context);
            }
            try {
                return callable.call();
            } finally {
                // 清除MDC上下文信息，避免造成内存泄漏
                MDC.clear();
            }
        };
    }

    private Runnable wrap(final Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            if (context != null) {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
