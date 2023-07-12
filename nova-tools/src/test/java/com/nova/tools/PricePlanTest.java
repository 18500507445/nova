package com.nova.tools;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.starter.redis.RedisService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
        for (int i = 0; i < 7200000; i++) {
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

    @Test
    public void demoA() {
        TimeInterval timer = DateUtil.timer();
        List<skuDTO> list1 = new ArrayList<>();
        List<skuDTO> list2 = new ArrayList<>();
        for (int i = 0; i < 4000000; i++) {
            skuDTO skuDTO = new skuDTO();
            skuDTO.setSkuId(String.valueOf(i));
            skuDTO.setCateId(String.valueOf(i));
            if (i < 500000) {
                list1.add(skuDTO);
            } else {
                list2.add(skuDTO);
            }
        }
        System.out.println("拼装耗时：" + timer.interval() + " ms");
        timer.restart();

        LinkedHashSet<skuDTO> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.addAll(list1);
        linkedHashSet.addAll(list2);

        System.out.println("list.size：" + linkedHashSet.size());
        System.out.println("处理：" + timer.interval() + " ms");
    }

    @Data
    @EqualsAndHashCode
    static class skuDTO {
        String skuId = "";
        String cateId = "";
    }


    @Test
    public void demoK() throws ExecutionException, InterruptedException {
        TimeInterval timer = DateUtil.timer();
        System.out.println("计时开始");
        CompletableFuture<Long> task1 = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(1300L);
            return 1L;
        });
        CompletableFuture<Long> task2 = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(400L);
            return 2L;
        });
        CompletableFuture<Long> task3 = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(3000L);
            return 3L;
        });
        CompletableFuture.allOf(task1, task2, task3).join();
        Long one = task1.get();
        Long two = task2.get();
        Long three = task3.get();
        System.out.println("one = " + one);
        System.out.println("two = " + two);
        System.out.println("three = " + three);
        System.out.println("耗时 = " + timer.interval());
    }

    public static void main(String[] args) {
        ArrayList<skuDTO> skuDTOS = new ArrayList<>();
        List<Object> collect = skuDTOS.stream().map(skuDTO::getSkuId).collect(Collectors.toList());
        System.out.println("collect = " + collect);
    }
}
