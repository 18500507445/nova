package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

public class SimpleCacheTest {

    @Before("")
    public void putTest() {
        final SimpleCache<String, String> cache = new SimpleCache<>();
        ThreadUtil.execute(() -> cache.put("key1", "value1"));
        ThreadUtil.execute(() -> cache.get("key1"));
        ThreadUtil.execute(() -> cache.put("key2", "value2"));
        ThreadUtil.execute(() -> cache.get("key2"));
        ThreadUtil.execute(() -> cache.put("key3", "value3"));
        ThreadUtil.execute(() -> cache.get("key3"));
        ThreadUtil.execute(() -> cache.put("key4", "value4"));
        ThreadUtil.execute(() -> cache.get("key4"));
        ThreadUtil.execute(() -> cache.get("key5", () -> "value5"));

        cache.get("key5", () -> "value5");
    }

    @Test
    public void getTest() {
        final SimpleCache<String, String> cache = new SimpleCache<>();
        cache.put("key1", "value1");
        cache.get("key1");
        cache.put("key2", "value2");
        cache.get("key2");
        cache.put("key3", "value3");
        cache.get("key3");
        cache.put("key4", "value4");
        cache.get("key4");
        cache.get("key5", () -> "value5");

        Assert.equals("value1", cache.get("key1"));
        Assert.equals("value2", cache.get("key2"));
        Assert.equals("value3", cache.get("key3"));
        Assert.equals("value4", cache.get("key4"));
        Assert.equals("value5", cache.get("key5"));
        Assert.equals("value6", cache.get("key6", () -> "value6"));
    }

    @Test
    public void getConcurrencyTest() {
        final SimpleCache<String, String> cache = new SimpleCache<>();
        final ConcurrencyTester tester = new ConcurrencyTester(9000);
        tester.test(() -> cache.get("aaa", () -> {
            ThreadUtil.sleep(200);
            return "aaaValue";
        }));

        Assert.isTrue(tester.getInterval() > 0);
        Assert.equals("aaaValue", cache.get("aaa"));
    }
}
