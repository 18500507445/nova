package com.nova.tools;

import com.nova.tools.demo.entity.Topic;
import com.starter.mongo.MongoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: mongo-starter测试类
 * @author: wzh
 * @date: 2023/4/22 21:09
 */
@SpringBootTest
public class TestMongo {

    @Resource
    private MongoService mongoService;

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
        topic.setCreated(new Date());
        topic.setDeleted(false);
        topic.setNote("计算机组成原理");
        mongoService.save(topic);
    }

    /**
     * 查询demo
     */
    @Test
    public void testFind() {
        List<Topic> all = mongoService.findAll(Topic.class);
        all.forEach(System.out::println);
    }
}
