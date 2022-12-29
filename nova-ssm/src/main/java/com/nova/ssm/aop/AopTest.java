package com.nova.ssm.aop;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/29 15:27
 */
public class AopTest {

    /**
     * 执行之前的方法
     */
    public void before() {
        System.out.println("我是执行之前");
    }

    /**
     * 执行之后的方法
     */
    public void after(Joinpoint joinpoint) {
        System.out.println("我是执行之后");
    }

    /**
     * 环绕
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("环绕开始");
        Object value = joinPoint.proceed();
        System.out.println("环绕结束，结果为：" + value);
        return value;
    }
}
