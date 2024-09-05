package com.nova.common.trace.http;

import com.nova.common.trace.Trace;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * @author: wzh
 * @description: HttpClient透传TraceId
 * @date: 2023/10/07 16:05
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String traceId = MDC.get(Trace.TRACE_ID);
        //当前线程调用中有traceId，则将该traceId进行透传
        if (traceId != null) {
            //添加请求体
            httpRequest.addHeader(Trace.TRACE_ID, traceId);
        }
    }

    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create()
                .addInterceptorFirst(new HttpClientTraceIdInterceptor())
                .build();
        client.execute(new HttpGet("https://www.baidu.com"));
    }

}
