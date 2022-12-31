package com.nova.design.action.interceptor.service.impl;

import com.nova.design.action.interceptor.service.Expression;

/**
 * @description: 抽象非终结符表达式
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public abstract class NonTerminalExpression implements Expression {

    Expression e1, e2;

    public NonTerminalExpression(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
}