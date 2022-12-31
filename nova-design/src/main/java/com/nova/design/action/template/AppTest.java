package com.nova.design.action.template;


import org.junit.jupiter.api.Test;

/**
 * @description: 模板模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        // 造一个布灵布灵的小房子...
        AbstractClass decorate = new DecorateOne();
        decorate.operation();
    }

}