package com.nova.book.design.action.template;

/**
 * @description: 算法的一些步骤延迟到子类中实现
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class DecorateOne extends AbstractClass {

    @Override
    protected void decorate() {
        System.out.println("自定义...");
        System.out.println("布灵布灵的装修风格...");
    }
}