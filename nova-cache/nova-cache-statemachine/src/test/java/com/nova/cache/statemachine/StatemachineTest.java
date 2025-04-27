package com.nova.cache.statemachine;

import cn.hutool.core.thread.ThreadUtil;
import com.nova.cache.statemachine.manager.facade.OrderFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: spring状态机测试类
 * @date: 2024/04/11 15:12
 */
@SpringBootTest
public class StatemachineTest {

    @Resource
    private OrderFacade orderFacade;

    @Test
    public void demoA() {
        orderFacade.create(1L);
        orderFacade.pay(1L);
        orderFacade.deliver(1L);
        orderFacade.receive(1L);

        ThreadUtil.sleep(200);

        orderFacade.create(2L);
        orderFacade.pay(2L);
        orderFacade.deliver(2L);
        orderFacade.receive(2L);
        System.err.println("全部订单状态：" + orderFacade.getOrders());
    }

}
