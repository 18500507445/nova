package com.nova.design.action.visitor.login.impl;


import com.nova.design.action.visitor.login.Login;
import com.nova.design.action.visitor.login.Visitor;
/**
 * @description: 爱奇艺访问者
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class IqyVisitor implements Visitor {
    @Override
    public void visit(Login login) {
        System.out.println("爱奇艺访问者");
        login.accept(this);
    }

//    @Override
//    public void visitYk(Login login) {
//        System.out.println("爱奇艺访问者");
//        login.accept(this);
//    }
}