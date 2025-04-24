package com.nova.common.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.nova.common.utils.thread.MdcTaskDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author: wzh
 * @description: 线程池配置
 * @date: 2022/7/18 16:24
 */
@Slf4j(topic = "ThreadPoolConfig")
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程池大小
     */
    private static final int CORE_POOL_SIZE = 50;

    /**
     * 最大可创建的线程数
     */
    private static final int MAX_POOL_SIZE = 200;

    /**
     * 队列最大长度
     */
    private static final int QUEUE_CAPACITY = 1024;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private static final int KEEP_ALIVE_SECONDS = 60;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator(new MdcTaskDecorator());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();
        return new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, namedThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                printException(r, t);
            }
        };
    }

    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            log.error(t.getMessage(), t);
        }
    }
}
