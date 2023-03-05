package com.nova.book.design.action.visitor.login;

/**
 * @description: 访问者接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public interface Visitor {

    /**
     * 对于访问者而言，登录是访问者的对象，我不关心是怎么登录的
     *
     * @param login
     */
    void visit(Login login);

}
