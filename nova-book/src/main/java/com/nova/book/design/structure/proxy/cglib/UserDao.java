package com.nova.book.design.structure.proxy.cglib;

/**
 * @description: CgLib代理：目标对象,没有实现任何接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class UserDao {

    public void save() {
        System.err.println("----已经保存数据!----");
    }

}
