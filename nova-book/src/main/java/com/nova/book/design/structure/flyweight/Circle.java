package com.nova.book.design.structure.flyweight;

/**
 * @description: 具体的共享对象
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class Circle implements Shape {

    private final String color;

    public Circle(String color) {
        this.color = color;
    }

    @Override
    public void draw() {
        System.out.println("圆（" + this.color + "）");
    }
}