package com.nova.tools.db;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.nova.starter.mongo.MongoService;
import com.nova.starter.mongo.entity.Page;
import com.nova.starter.mongo.wrapper.LambdaQueryWrapper;
import com.nova.starter.mongo.wrapper.Wrappers;
import com.nova.tools.demo.mongo.Topic;
import com.nova.tools.demo.mongo.User;
import com.nova.tools.demo.mongo.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @description: mongo-starter测试类
 * @author: wzh
 * @date: 2023/4/22 21:09
 */
@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoService mongoService;

    @Autowired
    private MongoTemplate primaryMongoTemplate;

    @Autowired
    private UserService userService;

    /**
     * 保存demo
     */
    @Test
    public void testSave() {
        Topic topic = new Topic();
        topic.setName("从器件角度看，计算机经历了五代变化。但从系统结构看，至今绝大多数计算机仍属于（  ）计算机。");
        topic.setType(1);
        topic.setSubject(1266661195234037761L);

        Map<Integer, String> select = new HashMap<>(16);
        select.put(0, "并行");
        select.put(1, "冯诺依曼");
        select.put(2, "智能");
        select.put(3, "串行");
        topic.setSelect(select);

        topic.setAnswer(1);
        topic.setCreateTime(DateUtil.now());
        topic.setDelFlag(false);
        topic.setNote("计算机组成原理");

        String simpleName = topic.getClass().getSimpleName().toLowerCase();
        mongoService.save(topic, simpleName + "_" + DateUtil.today());

        primaryMongoTemplate.save(topic);
    }

    /**
     * 查询demo
     */
    @Test
    public void testFind() {
        List<Topic> all = mongoService.findAll(Topic.class);
        all.forEach(System.err::println);
    }

    @Test
    public void testFindOne() {
        Topic one = mongoService.findOne(Topic.class, new String[]{"answer"}, new Object[]{1});

        primaryMongoTemplate.save(one);
        System.out.println("one = " + one);
    }


    /**
     * 第二种插入
     */
    @Test
    public void testInsert() {
        User user = new User();
        String now = DateUtil.now();
        user.setId(1L).setLoginName("wzh").setPassWord("123").setAge(28).setCreateTime(now).setUpdateTime(now);
        userService.save(user);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setPassWord("234");
        userService.updateById(user);
    }

    @Test
    public void testDelete() {
        userService.removeById(1L);
    }

    @Test
    public void testQuery() {
        User user = userService.getById(1L);
        System.err.println("user = " + JSONUtil.toJsonStr(user));
    }

    /**
     * 分页查询
     */
    @Test
    public void testQueryList() {
        User req = new User();
        req.setLoginName("wzh").setPassWord("123").setAge(28).setPageNo(1).setPageSize(20);
        LambdaQueryWrapper<User> query = Wrappers.<User>lambdaQuery()
                .eq(Objects.nonNull(req.getAge()), User::getAge, req.getAge())
                .eq(Objects.nonNull(req.getLoginName()), User::getLoginName, req.getLoginName())
                .eq(Objects.nonNull(req.getPassWord()), User::getPassWord, req.getPassWord())
                .eq(Objects.nonNull(req.getAge()), User::getAge, req.getAge())
                .between((Objects.nonNull(req.getStartTime()) && Objects.nonNull(req.getEndTime())), User::getCreateTime, req.getStartTime(), req.getEndTime());
        Page<User> page = userService.page(query, req.getPageNo(), req.getPageSize());
        System.err.println("page = " + JSONUtil.toJsonStr(page));
    }

}
