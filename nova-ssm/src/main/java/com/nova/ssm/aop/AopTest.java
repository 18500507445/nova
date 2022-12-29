package com.nova.ssm.aop;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/29 15:27
 */
public class AopTest {

    /**
     * 执行之后的方法
     */
    public void after() {
        System.out.println("我是执行之后");
    }

    /**
     * 执行之前的方法
     */
    public void before() {
        System.out.println("我是执行之前");
    }
}
