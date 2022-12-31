package com.nova.design.structure.decorator.people;


/**
 * @description: 具体装饰
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class CarDecorator extends AbstractPersonDecorator {

    public CarDecorator(Person person) {
        super(person);
    }

    @Override
    public void run() {
        super.person.run();
        System.err.println("开车...");
    }
}


