package com.nova.tools.demo.designpatterns.single;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/11/7 10:43
 */

/**
 * 单例模式（Singleton Pattern）是 Java 中最简单的设计模式之一。这种类型的设计模式属于创建型模式，它提供了一种创建对象的最佳方式。
 * 这种模式涉及到一个单一的类，该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。
 * 注意
 * 1、单例类只能有一个实例。
 * 2、单例类必须自己创建自己的唯一实例。
 * 3、单例类必须给所有其他对象提供这一实例。
 * <p>
 * 意图：保证一个类仅有一个实例，并提供一个访问它的全局访问点。
 * 主要解决：一个全局使用的类频繁地创建与销毁。
 * 何时使用：当您想控制实例数目，节省系统资源的时候。
 * 如何解决：判断系统是否已经有这个单例，如果有则返回，如果没有则创建。
 * 关键代码：构造函数是私有的。
 */
public class SingleDemo {
    //创建一个single的一个对象
    private static SingleDemo SINGLE = new SingleDemo();

    //构造函数私有，该类不能被实例化
    private SingleDemo() {
    }


    //获取一个可用的对象
    public static SingleDemo getInstance() {
        return SINGLE;
    }

    public void showMessage() {
        System.out.println("Hello world");
    }
}
