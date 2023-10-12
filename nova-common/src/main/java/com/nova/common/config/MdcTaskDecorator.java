package com.nova.common.config;

import com.nova.common.trace.Trace;
import com.nova.common.trace.TraceContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * @author: wzh
 * @description 异步任务支持traceId
 * @date: 2023/09/27 17:11
 */
public class MdcTaskDecorator implements TaskDecorator {

    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
//        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
//                if (MapUtil.isNotEmpty(copyOfContextMap)) {
//                    // 现在：@Async线程上下文！ 恢复Web线程上下文的MDC数据
//                    MDC.setContextMap(copyOfContextMap);
//                }
                MDC.put(Trace.TRACE_ID, TraceContext.genSpanId());
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
