package com.nova.design.structure.proxy.service.impl;


import com.nova.design.structure.proxy.service.JdbcService;

/**
 * @description: jdbc实现类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class JdbcServiceImpl implements JdbcService {

    @Override
    public void selectOne() {
        System.out.println("hello...");
    }
}
