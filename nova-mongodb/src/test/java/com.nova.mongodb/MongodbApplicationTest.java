package com.nova.mongodb;

import com.nova.mongodb.entity.Topic;
import com.nova.mongodb.utils.MongodbUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/9/8 21:50
 */
@SpringBootTest
public class MongodbApplicationTest {

    @Autowired
    private MongodbUtils mongodbUtils;

    public static void main(String[] args) {

    }

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
        MongodbUtils.save(topic);
    }

    @Test
    public void testFind() {
        List<Topic> all = MongodbUtils.findAll(Topic.class);
        all.forEach(System.out::println);
    }
}
