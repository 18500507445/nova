package com.nova.book.design.action.command.cook.service.impl;

import com.nova.book.design.action.command.cook.Cook;
import com.nova.book.design.action.command.cook.service.CuisineService;

/**
 * @description: 上海菜实现类
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class ShanghaiCuisineImpl implements CuisineService {

    private final Cook cook;

    public ShanghaiCuisineImpl(Cook cook) {
        this.cook = cook;
    }

    @Override
    public void cook() {
        System.out.println("上海菜");
        this.cook.doing();
    }
}