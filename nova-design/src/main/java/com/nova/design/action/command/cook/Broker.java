package com.nova.design.action.command.cook;

import com.nova.design.action.command.cook.service.CuisineService;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @description: 命令调用类
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
public class Broker {

    private final List<CuisineService> cuisineServiceList = Lists.newArrayList();

    public void takeOrder(CuisineService cuisineService) {
        this.cuisineServiceList.add(cuisineService);
    }

    public void placeOrders() {
        for (CuisineService cuisineService : this.cuisineServiceList) {
            cuisineService.cook();
        }
        this.cuisineServiceList.clear();
    }
}