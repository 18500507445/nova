package com.nova.search.elasticsearch;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.nova.search.elasticsearch.entity.User;
import com.nova.search.elasticsearch.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: wzh
 * @description: JPA写法测试类
 * @date: 2024/04/28 09:44
 */
@Slf4j
@SpringBootTest
public class RepositoryTest {

    @Resource
    private UserRepository userRepository;

    /**
     * 单条新增，支持按照id进行修改
     */
    @Test
    public void insertOne() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("张三_" + i);
            user.setPassword("password_" + i);
            user.setCreateTime(new Date());
            userRepository.save(user);
        }
    }

    //测试修改
    @Test
    public void update() {
        User user = new User();
        user.setId(1L);
        user.setUserName("张大千");
        userRepository.save(user);
    }

    //批量插入
    @Test
    public void insertList() {
        List<User> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("张三_" + i);
            user.setPassword("password_" + i);
            user.setCreateTime(new Date());
            list.add(user);
        }
        userRepository.saveAll(list);
    }


    //根据id查询 + 多条
    @Test
    public void findById() {
        Optional<User> entity = userRepository.findById(1);
        entity.ifPresent(user -> Console.log("单条：{}", JSONUtil.toJsonStr(user)));

        List<Integer> idList = Arrays.asList(1, 2, 3, 4, 5);
        Iterable<User> all = userRepository.findAllById(idList);
        Console.log("多条：{}", JSONUtil.toJsonStr(all));
    }

    //查询所有
    @Test
    public void findAll() {
        Iterable<User> iterable = userRepository.findAll();
        Console.log("jsonUser：{} ", JSONUtil.toJsonStr(iterable));
    }

    //分页查询（支持简单分页、排序）
    @Test
    public void page() {
        Pageable page = PageRequest.of(0, 2, Sort.Direction.ASC, "id");
        Page<User> pageList = userRepository.findAll(page);
        Console.log("pageList：{} ", JSONUtil.toJsonStr(pageList));
    }

    //根据id删除  + 多条
    @Test
    public void deleteOne() {
        User user = new User();
        user.setId(1L);
        userRepository.delete(user);

        List<Integer> idList = Arrays.asList(1, 2, 3, 4, 5);
        userRepository.deleteAllById(idList);
    }


    //jpa写法
    @Test
    public void jpaTest() {
        Page<User> page = userRepository.getUserByUserNameLike("张三", PageRequest.of(0, 3, Sort.Direction.DESC, "id"));
        System.out.println("page = " + JSONUtil.toJsonStr(page));
    }



}
