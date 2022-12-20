package com.nova.cache;

import cn.hutool.core.util.ObjectUtil;
import com.nova.cache.core.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheApplicationTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() {
        String group = "nova-redis:%s";
        String key = String.format(group, "testRedis");

        redisService.set(key, "1", 5 * 60L);
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            System.out.println(o.toString());
        }
    }
}
