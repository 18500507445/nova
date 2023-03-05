package com.nova.book.design.action.mediator;

import org.junit.jupiter.api.Test;

/**
 * @description: 终结者模式测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        User zs = new User("张三");
        User ls = new User("李四");
        // 多个用户可以向聊天室发送消息
        zs.sendMessage("how are you?");
        ls.sendMessage("im fine.");
    }

}