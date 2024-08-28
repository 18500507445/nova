package com.nova.tools.db;

import cn.hutool.core.date.DateUtil;
import com.nova.starter.mongo.MongoService;
import com.nova.tools.demo.mongo.Topic;
import com.nova.tools.demo.mongo.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private UserMapper userMapper;

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
     * 第二种，JPA-插入、修改
     */
    @Test
    public void testInsert() {
        Topic topic = new Topic();
        topic.setName("测试第三条");
        topic.setId("66ce880ac7e7287c9f7d0caa");

        Map<Integer, String> select = new HashMap<>(16);
        select.put(0, "并行");
        select.put(1, "冯诺依曼");
        select.put(2, "智能");
        select.put(3, "串行");
        topic.setSelect(select);

        topic.setAnswer(0).setType(0).setSubject(0L);
        topic.setNote("备注").setImage("图片");
        topic.setCreateTime(DateUtil.now());
        topic.setDelFlag(false);

        topic.setPageNo(0).setPageSize(0);
        //fixme 不要用insert 它只会插入
        userMapper.save(topic);
    }

    //删除
    @Test
    public void testDelete() {
        userMapper.deleteById("66ce8a0a46594551bb4968f0");
    }

    //简单查询
    @Test
    public void simpleQuery() {
        Optional<Topic> one = userMapper.findById("66ce880ac7e7287c9f7d0caa");
        one.ifPresent(System.err::println);
    }

    //高级查询
    @Test
    public void advancedQuery() {
        int currentPage = 1;
        int pageSize = 2;

        Topic query = new Topic();
        query.setName("测试第三条");

        // 设置ExampleMatcher来定义匹配规则
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase() // 忽略大小写
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // 包含

        // 使用条件对象和匹配器创建Example实例
        Example<Topic> example = Example.of(query, matcher);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);

        /**
         * Example 这种方式更适合于属性匹配（如字符串的模糊匹配）。对于比较运算（如大于、小于等），使用Spring Data MongoDB提供的Criteria和Query对象来构建更复杂的查询条件。
         */
        Page<Topic> all = userMapper.findAll(example, pageRequest);
        all.forEach(System.err::println);
    }


}
