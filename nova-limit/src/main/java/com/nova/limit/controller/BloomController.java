package com.nova.limit.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.google.common.hash.Funnels;
import com.nova.limit.config.BloomFilterHelper;
import com.nova.limit.utils.JedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 布隆过滤器
 * @author: wzh
 * @date: 2023/4/21 17:23
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class BloomController {

    private final JedisUtil jedisUtil;

    private final RedissonClient redissonClient;

    /**
     * redis
     */
    @PostMapping("redis")
    public void redis() {
        String redisKey = "redisBloom";
        int expectedInsertions = 10000;
        double fpp = 0.03;

        jedisUtil.del(redisKey);

        BloomFilterHelper<Object> bloomFilterHelper = new BloomFilterHelper<>(Funnels.integerFunnel(), expectedInsertions, fpp);
        int j = 0;
        // 添加100个元素
        final TimeInterval timer = DateUtil.timer();
        for (int i = 0; i < 100; i++) {
            jedisUtil.add(bloomFilterHelper, redisKey, i);
        }
        System.err.println("布隆过滤器添加100个值，耗时：" + timer.interval() + "ms");
        timer.restart();
        for (int i = 0; i < 101; i++) {
            boolean result = jedisUtil.contains(bloomFilterHelper, redisKey, i);
            if (!result) {
                j++;
            }
        }
        System.err.println("漏掉了" + j + "个,验证结果耗时：" + timer.interval() + "ms");
    }

    @PostMapping("redisson")
    public void redisson() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("phoneList");
        //初始化布隆过滤器：预计元素为1000000L,误差率为3%
        bloomFilter.tryInit(1000000L, 0.03);
        //将1,2,3插入到布隆过滤器中
        List<String> list = Arrays.asList("1", "2", "3");
        list.forEach(bloomFilter::add);

        //判断下面号码是否在布隆过滤器中
        System.err.println(bloomFilter.contains("1"));
        System.err.println(bloomFilter.contains("4"));
    }
}
