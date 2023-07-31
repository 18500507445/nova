package com.nova.book.design.structure.proxy;


import com.nova.book.design.structure.proxy.service.JdbcService;
import com.nova.book.design.structure.proxy.service.impl.JdbcServiceImpl;

/**
 *
 * 代理类
 * @description: 减少 JdbcServiceImpl 对象加载的内存占用
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class JdbcProxy implements JdbcService {

    JdbcServiceImpl jdbcServiceImpl;

    @Override
    public void selectOne() {
        if (this.jdbcServiceImpl == null) {
            System.err.println("加载...");
            this.jdbcServiceImpl = new JdbcServiceImpl();
        }
        this.jdbcServiceImpl.selectOne();
    }
}
