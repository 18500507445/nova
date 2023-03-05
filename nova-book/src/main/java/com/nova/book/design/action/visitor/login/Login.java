package com.nova.book.design.action.visitor.login;


/**
 * @description: 登录接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public interface Login {

    /**
     * 对于登录而言，访问者是被接受的,我根本不知道访问者是谁
     *
     * @param visitor
     */
    void accept(Visitor visitor);

}
