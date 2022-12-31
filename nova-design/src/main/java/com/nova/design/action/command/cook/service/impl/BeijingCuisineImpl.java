package com.nova.design.action.command.cook.service.impl;

import com.nova.design.action.command.cook.Cook;
import com.nova.design.action.command.cook.service.CuisineService;

/**
 * @description: 北京菜实现类
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class BeijingCuisineImpl implements CuisineService {

    private final Cook cook;

    public BeijingCuisineImpl(Cook cook) {
        this.cook = cook;
    }

    @Override
    public void cook() {
        System.out.println("北京菜");
        this.cook.doing();
    }
}