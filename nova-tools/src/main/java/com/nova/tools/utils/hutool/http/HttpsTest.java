package com.nova.tools.utils.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpsTest {

    /**
     * 测试单例的SSLSocketFactory是否有线程安全问题
     */
    @Test
    @Ignore
    public void getTest() {
        final AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            ThreadUtil.execute(() -> {
                final String s = HttpUtil.get("https://www.baidu.com/");
                Console.log(count.incrementAndGet());
            });
        }
        ThreadUtil.sync(this);
    }
}
