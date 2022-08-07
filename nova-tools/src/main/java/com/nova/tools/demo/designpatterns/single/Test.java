package com.nova.tools.demo.designpatterns.single;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/11/7 10:47
 */

public class Test {
    public static void main(String[] args) {
        //测试单利
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        if (instance1 == instance2) {
            System.out.println("same");
        }

    }
}
