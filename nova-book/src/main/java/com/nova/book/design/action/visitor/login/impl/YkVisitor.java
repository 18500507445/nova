package com.nova.book.design.action.visitor.login.impl;

import com.nova.book.design.action.visitor.login.Login;
import com.nova.book.design.action.visitor.login.Visitor;

/**
 * @description: 优酷访问者
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class YkVisitor implements Visitor {
    @Override
    public void visit(Login login) {
        System.err.println("优酷访问者");
        login.accept(this);
    }

//    @Override
//    public void visitYk(Login login) {
//        System.err.println("优酷访问者");
//        login.accept(this);
//    }
}
