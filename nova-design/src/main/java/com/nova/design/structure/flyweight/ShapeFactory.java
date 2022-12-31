package com.nova.design.structure.flyweight;

import java.util.HashMap;

/**
 * 管理所有的共享对象
 * @description: 当用户请求时提供一个已创建的实例 或者 不存在时创建一个新的实例
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class ShapeFactory {

    private static final HashMap<String, Shape> circleMap = new HashMap<>();

    public static Shape getCircle(String color) {
        Circle circle = (Circle) circleMap.get(color);
        if (circle == null) {
            circle = new Circle(color);
            circleMap.put(color, circle);
            System.out.println("创建圆: " + color);
        }
        return circle;
    }
}