package com.nova.book.design.action.interpreter;

import com.nova.book.design.action.interpreter.service.impl.MinusOperation;
import com.nova.book.design.action.interpreter.service.impl.PlusOperation;
import com.nova.book.design.action.interpreter.service.impl.TerminalExpression;
import org.junit.jupiter.api.Test;

/**
 * @description: 解释器模式测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        Context context = new Context();
        TerminalExpression a = new TerminalExpression("a");
        TerminalExpression b = new TerminalExpression("b");
        TerminalExpression c = new TerminalExpression("c");
        context.add(a, 4);
        context.add(b, 8);
        context.add(c, 2);
        int result = new MinusOperation(new PlusOperation(a, b), c).interpreter(context);
        System.err.println("result = " + result);
    }

}