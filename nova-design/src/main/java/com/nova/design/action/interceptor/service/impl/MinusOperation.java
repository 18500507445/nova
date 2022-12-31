package com.nova.design.action.interceptor.service.impl;

import com.nova.design.action.interceptor.Context;
import com.nova.design.action.interceptor.service.Expression;

/**
 * @description: 减法表达式实现类
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class MinusOperation extends NonTerminalExpression {

    public MinusOperation(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public int interpreter(Context context) {
        // 将两个表达式相减
        return this.e1.interpreter(context) - this.e2.interpreter(context);
    }
}