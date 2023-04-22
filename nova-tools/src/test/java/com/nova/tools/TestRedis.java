package com.nova.tools;

import com.starter.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description: redis-starter测试类
 * @author: wzh
 * @date: 2023/4/22 21:08
 */
@SpringBootTest
public class TestRedis {

    @Resource
    private RedisService redisService;

    @Test
    public void testRedis() {
        final Object o = redisService.get("234");
        System.out.println("o = " + o);
    }
}
