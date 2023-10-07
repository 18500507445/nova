package com.nova.common.trace;

import cn.hutool.core.util.ObjectUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author: wzh
 * @description: OkHttp透传TraceId
 * @date: 2023/10/07 16:05
 */
public class OkHttpTraceIdInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String traceId = MDC.get(Trace.TRACE_ID);
        Request request = null;
        if (traceId != null) {
            //添加请求体
            request = chain.request().newBuilder().addHeader(Trace.TRACE_ID, traceId).build();
        }
        assert request != null;
        return chain.proceed(request);
    }

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new OkHttpTraceIdInterceptor())
                .build();
        Request request = new Request.Builder().url("https://www.baidu.com").build();
        Response httpResponse = client.newCall(request).execute();
        if (httpResponse.code() == 200) {
            if (ObjectUtil.isNotNull(httpResponse.body())) {
                String result = httpResponse.body().string();
                System.err.println("result = " + result);
            }
        }
    }
}