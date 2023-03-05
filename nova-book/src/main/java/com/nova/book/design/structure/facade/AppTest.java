package com.nova.book.design.structure.facade;


import org.junit.jupiter.api.Test;

/**
 * @description: 外观模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        Facade facade = new Facade();
        facade.handle1();
        facade.handle2();
    }

}