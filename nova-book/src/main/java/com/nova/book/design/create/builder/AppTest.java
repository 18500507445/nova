package com.nova.book.design.create.builder;


import org.junit.jupiter.api.Test;

/**
 * @description: 建造者测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        User user = User.builder()
                .id(1L)
                .nickname("小明")
                .build();
        System.err.println(user);
    }

}