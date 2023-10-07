package com.nova.common.trace;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author: wzh
 * @description: RestTemplate透传TraceId
 * @date: 2023/10/07 16:05
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {

    @NotNull
    @Override
    public ClientHttpResponse intercept(@NotNull HttpRequest httpRequest, @NotNull byte[] bytes, @NotNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String traceId = MDC.get(Trace.TRACE_ID);
        if (traceId != null) {
            httpRequest.getHeaders().add(Trace.TRACE_ID, traceId);
        }
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}