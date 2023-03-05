package com.nova.book.design.action.command;

import com.nova.book.design.action.command.cook.Broker;
import com.nova.book.design.action.command.cook.Cook;
import com.nova.book.design.action.command.cook.service.impl.BeijingCuisineImpl;
import com.nova.book.design.action.command.cook.service.impl.ShanghaiCuisineImpl;
import org.junit.jupiter.api.Test;

/**
 * @description: 命令模式测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        // 菜
        BeijingCuisineImpl beijingCuisine = new BeijingCuisineImpl(new Cook());
        ShanghaiCuisineImpl shanghaiCuisine = new ShanghaiCuisineImpl(new Cook());

        // 点单
        Broker broker = new Broker();
        broker.takeOrder(beijingCuisine);
        broker.takeOrder(shanghaiCuisine);

        // 下单
        broker.placeOrders();
    }

}