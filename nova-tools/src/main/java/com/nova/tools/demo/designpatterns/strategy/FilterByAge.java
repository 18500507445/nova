package com.nova.tools.demo.designpatterns.strategy;


import com.nova.tools.demo.entity.People;

/**
 * @Description: 策略接口实现类
 * @Author: wangzehui
 * @Date: 2021/10/16 12:35 下午
 */
public class FilterByAge implements MyPredicate<People> {

    @Override
    public Boolean filter(People people) {
        return people.getAge() < 17;
    }
}
