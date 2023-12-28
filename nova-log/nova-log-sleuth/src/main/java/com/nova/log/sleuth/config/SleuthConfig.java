package com.nova.log.sleuth.config;

import brave.sampler.Sampler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description 配置类
 * @date: 2023/12/28 10:46
 */
@Configuration
public class SleuthConfig {

    /**
     * ALWAYS_SAMPLE:表示采样策略始终对所有请求进行采样，即始终生成跟踪
     * NEVER_SAMPLE：不对任何请求进行采样，即始终拒绝生成跟踪数据
     */
    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    /**
     * RestTemplate，SpringCloud-Sleuth针对RestTemplate的调用也实现了TraceId的创建与传递，
     * 当然前提是RestTemplate由容器管理（无需进行额外的配置，只需要引SpringCloud-Sleuth 依赖即可，挺牛皮）
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    /**
     * Spring类型线程池，交给spring管理
     */
    @Bean(name = "springExecutor")
    public Executor springExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("ThreadPoolTaskExecutor");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * jdk线程池，交给spring管理
     */
    @Bean(name = "jdkExecutor")
    public Executor jdkExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));
        threadPoolExecutor.setThreadFactory(new CustomizableThreadFactory("ThreadPoolExecutor"));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolExecutor;
    }

}
