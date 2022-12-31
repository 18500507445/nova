package com.nova.design.structure.decorator.people;

/**
 * @description: 抽象的装饰器
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public abstract class AbstractPersonDecorator implements Person {

    protected final Person person;

    public AbstractPersonDecorator(Person person) {
        this.person = person;
    }
}