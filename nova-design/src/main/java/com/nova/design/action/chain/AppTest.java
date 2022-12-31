package com.nova.design.action.chain;

import com.nova.design.action.chain.handler.AbstractHandler;
import com.nova.design.action.chain.handler.Level1Handler;
import com.nova.design.action.chain.handler.Level2Handler;
import org.junit.jupiter.api.Test;

/**
 * @description: 责任链测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        AbstractHandler abstractHandler = new Level2Handler().appendNext(new Level1Handler());
        boolean result = abstractHandler.approve("通过");
        System.out.println(result);
    }
}
