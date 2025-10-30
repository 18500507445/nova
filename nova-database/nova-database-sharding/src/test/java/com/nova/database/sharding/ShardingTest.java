package com.nova.database.sharding;

import com.nova.common.utils.random.RandomUtils;
import com.nova.database.sharding.repository.Order;
import com.nova.database.sharding.repository.OrderMapper;
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
        for (int i = 1; i <= 50; i++) {
            Order order = new Order();
            order.setOrderId(i);
            order.setUserName(RandomUtils.randomName());
            order.setCreateTime(new Date());
            orderMapper.insert(order);
        }
    }
}
