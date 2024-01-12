package com.nova.common.utils.thread;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author: wzh
 * @description 异步任务支持traceId
 * @date: 2023/09/27 17:11
 */
@SuppressWarnings("NullableProblems")
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (MapUtils.isNotEmpty(copyOfContextMap)) {
                    // 现在：@Async线程上下文！ 恢复Web线程上下文的MDC数据
                    MDC.setContextMap(copyOfContextMap);
                }
//                MDC.put(Trace.TRACE_ID, TraceContext.getTraceId());
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
