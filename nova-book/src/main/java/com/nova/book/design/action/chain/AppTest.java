package com.nova.book.design.action.chain;

import com.nova.book.design.action.chain.handler.AbstractHandler;
import com.nova.book.design.action.chain.handler.LevelAHandler;
import com.nova.book.design.action.chain.handler.LevelBHandler;
import org.junit.jupiter.api.Test;

/**
 * @description: 责任链测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        AbstractHandler abstractHandler = new LevelBHandler().appendNext(new LevelAHandler());
        boolean result = abstractHandler.approve("通过");
        System.out.println(result);
    }
}
