package com.nova.design.action.interpreter.service.impl;

import com.nova.design.action.interpreter.Context;
import com.nova.design.action.interpreter.service.Expression;

/**
 * @description: 终结符表达式
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class TerminalExpression implements Expression {

    public String variable;

    public TerminalExpression(String variable) {
        this.variable = variable;
    }

    @Override
    public int interpreter(Context context) {
        return context.lookup(this);
    }
}