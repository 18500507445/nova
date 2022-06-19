package com.nova.tools.demo.designpatterns.strategy;

/**
 * @Description: 策略接口 泛型
 * @Author: wangzehui
 * @Date: 2021/10/16 12:34 下午
 */
public interface MyPredicate<T> {

    Boolean filter(T t);
}
