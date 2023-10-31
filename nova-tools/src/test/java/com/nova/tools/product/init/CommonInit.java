package com.nova.tools.product.init;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import com.nova.common.trace.Trace;
import com.nova.common.trace.TraceContext;
import com.nova.common.utils.spring.EnvUtils;
import com.nova.tools.product.entity.ChannelConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description 配置初始化
 * @date: 2023/10/31 15:56
 */
@Component
@Slf4j(topic = "ConfigInit")
public class CommonInit {

    //<name，config>
    public static Map<String, ChannelConfig> CONFIG_MAP = new HashMap<>();

    //单核线程，没有救急，队列64、丢弃策略
    private static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(
            1,
            1,
            30L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(64),
            new ThreadFactoryBuilder().setNamePrefix("ConfigInit").build(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    public static <T> Callable<T> wrap(final Callable<T> callable, final String traceId) {
        return () -> {
            if (StrUtil.isNotEmpty(traceId)) {
                TraceContext.setCurrentTrace(traceId);
            }
            try {
                return callable.call();
            } finally {
                TraceContext.removeTrace();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final String traceId) {
        return () -> {
            if (StrUtil.isNotEmpty(traceId)) {
                TraceContext.setCurrentTrace(traceId);
            }
            try {
                runnable.run();
            } finally {
                TraceContext.removeTrace();
            }
        };
    }


    @PostConstruct
    void initConfig() {
        List<ChannelConfig> configList = new ArrayList<>();
        ChannelConfig jdConfig1 = ChannelConfig.builder().id(1L).name("jd").env(1).key("key").build();
        ChannelConfig jdConfig2 = ChannelConfig.builder().id(2L).name("jd").env(2).key("key").build();
        ChannelConfig yxConfig1 = ChannelConfig.builder().id(3L).name("yx").env(1).key("key").build();
        ChannelConfig yxConfig2 = ChannelConfig.builder().id(4L).name("yx").env(2).key("key").build();
        configList.add(jdConfig1);
        configList.add(jdConfig2);
        configList.add(yxConfig1);
        configList.add(yxConfig2);
        boolean isPro = EnvUtils.isPro();
        //过滤当前环境的配置
        Map<String, List<ChannelConfig>> collect = configList.stream().collect(Collectors.groupingBy(ChannelConfig::getName));
        for (Map.Entry<String, List<ChannelConfig>> map : collect.entrySet()) {
            List<ChannelConfig> value = map.getValue();
            ChannelConfig result = value.stream()
                    .filter(channelConfig -> isPro ? channelConfig.getEnv().equals(1) : channelConfig.getEnv().equals(2))
                    .findFirst()
                    .orElse(null);
            CONFIG_MAP.put(map.getKey(), result);
        }
    }

    /**
     * 异步刷新缓存
     */
    public void refresh() {
        System.err.println("修改配置，进行刷新缓存");
        EXECUTOR_POOL.submit(wrap(this::refresh, MDC.get(Trace.TRACE_ID)));
    }
}
