package com.nova.search.elasticsearch;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.common.utils.common.JsonUtils;
import com.nova.common.utils.random.RandomUtils;
import com.nova.search.elasticsearch.repository.User;
import com.nova.search.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description: es工具类测试
 * @date: 2024/04/28 16:10
 */
@Slf4j(topic = "EsUtilsTest")
@SpringBootTest
public class EsUtilsTest {

    public static final String INDEX_NAME = "test_create_index";

    @Test
    public void createIndex() {
        boolean result = EsUtils.indexCreate(INDEX_NAME);
        Console.error("result：{} " + result);
    }

    public static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
            8, 16, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512),
            new ThreadFactoryBuilder().setNamePrefix("completableFuture").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Test
    public void save() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("张三_" + i);
            user.setPassword("password_" + i);
            user.setCreateTime(new Date());
            EsUtils.save(user);
        }
    }

    //批量插入，性能测试，4线程，开大线程数据存在丢失
    @Test
    public void insertList() {
        List<User> list = new ArrayList<>();
        for (int i = 1; i <= 1000000; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName(RandomUtils.randomName());
            user.setPassword(RandomUtils.randomString(12));
            user.setIdCard(RandomUtils.randomIdCard());
            user.setAddress(RandomUtils.randomAddress());
            user.setSex(RandomUtils.randomInt(1, 3));
            user.setAge(RandomUtils.randomInt(18, 100));
            user.setCreateTime(new Date());
            list.add(user);
        }
        TimeInterval timer = DateUtil.timer();
        List<List<User>> split = ListUtil.split(list, 10000);
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (List<User> users : split) {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                EsUtils.saveAll(users);
                return users.size();
            }, POOL).thenAccept(i -> log.info("插入：{} 条", i));
            completableFutures.add(future);
        }
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        Console.error("耗时 = {} ms", timer.interval());
    }

    @Test
    public void count() {
        long count = EsUtils.count(User.class);
        System.err.println("count = " + count);
    }

    @Test
    public void findById() {
        User user = EsUtils.findById(1L, User.class);
        System.out.println("user = " + JsonUtils.toJson(user));
        System.out.println("hutool = " + JSONUtil.toJsonStr(user));
        System.out.println("fastjson = " + JSONObject.toJSONString(user));
    }

    @Test
    public void findAllById() {
        List<Integer> idList = Arrays.asList(1, 2, 3, 4, 5);
        List<User> list = EsUtils.findAllById(idList, User.class);
        System.out.println("list = " + JSONUtil.toJsonStr(list));
    }

    @Test
    public void findAll() {
        List<User> list = EsUtils.findAll(User.class);
        System.out.println("list = " + JSONUtil.toJsonStr(list));
    }

    @Test
    public void findPage() {
        Pageable page = PageRequest.of(0, 2, Sort.Direction.ASC, "id");
        Page<User> pageList = EsUtils.findAll(page, User.class);
        Console.log("pageList：{} ", JSONUtil.toJsonStr(pageList));
    }

    @Test
    public void delete() {
        List<Integer> idList = Arrays.asList(1, 2, 3, 4, 5);
        EsUtils.deleteAllById(idList, User.class);
    }

}
