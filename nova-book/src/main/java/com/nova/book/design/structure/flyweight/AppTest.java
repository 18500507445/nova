package com.nova.book.design.structure.flyweight;


import org.junit.jupiter.api.Test;

/**
 * @description: 享元模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void demoA() {
        for (int i = 0; i < 5; i++) {
            Circle circle = (Circle) ShapeFactory.getCircle("Blue");
            circle.draw();
        }
    }

    @Test
    public void demoB() {
        //放在Heap中
        String s = "Hello World";
        //放在常量池中
        String newValue = s.intern();

        System.err.println(newValue);
    }

}