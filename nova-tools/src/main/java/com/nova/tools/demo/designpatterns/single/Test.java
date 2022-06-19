package com.nova.tools.demo.designpatterns.single;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/11/7 10:47
 */

public class Test {
    public static void main(String[] args) {
        //测试单利
        SingleDemo instance = SingleDemo.getInstance();
        instance.showMessage();
    }
}
