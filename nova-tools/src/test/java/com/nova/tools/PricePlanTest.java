package com.nova.tools;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import com.starter.redis.RedisService;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description 定价测试类
 * @date: 2023/07/07 13:33
 */
@Slf4j(topic = "PricePlan")
@SpringBootTest
public class PricePlanTest {

    @Resource
    private RedisService redisService;

    public static final String PRODUCT_POLL = "productPoll";

    public static final String SPECIAL_POLL = "specialPoll";

    /**
     * 720w数据入队列，耗时120，内存占用310mb
     */
    @Test
    public void savePool() {
        TimeInterval timer = DateUtil.timer();
        //字符串拼接skuId-cateId-加价率-是否允许超过电商价-costPrice(成本价)-elePrice(JD电商价)
        List<Object> as = new ArrayList<>();
        for (int i = 0; i < 5000000; i++) {
            as.add(RandomUtil.randomNumbers(12) + "-" + RandomUtil.randomNumbers(8) + "-10.1" + "-1" + "-" + RandomUtil.randomNumbers(4) + "-" + RandomUtil.randomNumbers(4));
        }
        List<List<Object>> partition = ListUtil.partition(as, 80000);
        for (List<Object> objects : partition) {
            redisService.setList(PRODUCT_POLL, objects);
        }
        System.out.println("redis IO耗时：" + timer.interval());
    }

    /**
     * 添加或修改特殊任务，删除旧的，更新集合
     */
    @Test
    public void specialPoll() {
        List<Object> as = new ArrayList<>();
        for (int i = 0; i < 80000; i++) {
            as.add(RandomUtil.randomNumbers(8));
        }
        List<List<Object>> partition = ListUtil.partition(as, 80000);
        for (List<Object> objects : partition) {
            redisService.setSet(SPECIAL_POLL, objects.toArray());
        }
    }

    @Data
    @EqualsAndHashCode
    static class skuDTO {
        String skuId = "";
        String cateId = "";
        Integer poolType;
    }

    @Test
    public void testObjectByteSize() {
        List<Object> as = new ArrayList<>();
        for (int i = 0; i < 5000000; i++) {
            skuDTO skuDTO = new skuDTO();
            skuDTO.setSkuId(RandomUtil.randomNumbers(12));
            skuDTO.setCateId(RandomUtil.randomNumbers(8));
            skuDTO.setPoolType(1);
            as.add(skuDTO);
        }
        System.out.println("占用：" + ObjectSizeCalculator.getObjectSize(as) / 1024 / 1024 + "mb");
    }

}
