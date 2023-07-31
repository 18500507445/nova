package com.nova.book.design.action.visitor.login.impl;

import com.nova.book.design.action.visitor.login.Login;
import com.nova.book.design.action.visitor.login.Visitor;

/**
 * @description: qq登录实现类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class QQLogin implements Login {
    @Override
    public void accept(Visitor visitor) {
        System.err.println(visitor.getClass().getSimpleName() + "-QQ登录");
    }
}
