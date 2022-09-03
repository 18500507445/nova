package com.nova.redis;

import cn.hutool.core.util.ObjectUtil;
import com.nova.redis.core.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisApplicationTest {

    @Autowired
    private RedisService redisService;

    public static void main(String[] args) {

    }

    @Test
    public void testRedis() {
        String key = "nova-redis-key";
        redisService.set(key, "1", 5 * 60L);
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            System.out.println(o.toString());
        }
    }
}
