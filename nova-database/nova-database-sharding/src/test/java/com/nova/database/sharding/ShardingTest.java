package com.nova.database.sharding;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: wzh
 * @description: sharding-测试类
 * @date: 2025/10/30 11:05
 */
@SpringBootTest
public class ShardingTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void demoA() {
        for (int i = 1; i <= 5; i++) {
            Order order = new Order();
            order.setOrderId(i);
            order.setCreateTime(new Date());
            orderMapper.insert(order);
        }
    }
}
