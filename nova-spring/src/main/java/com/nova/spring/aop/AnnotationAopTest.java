package com.nova.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @description: 注解实现切面
 * @author: wangzehui
 * @date: 2022/12/29 20:13
 */
@Component
@Aspect
public class AnnotationAopTest {

    @Before("execution(* com.nova.spring.entity.Card.test(..))")
    public void before(JoinPoint point) {
        System.out.println("before：参数列表：" + Arrays.toString(point.getArgs()));
        System.out.println("before：我是之前执行的内容！");
    }

    @AfterReturning(value = "execution(* com.nova.spring.entity.Card.test(..))", returning = "returnVal")
    public void after(Object returnVal) {
        System.out.println("after：方法已返回，结果为：" + returnVal);
    }

    @Around("execution(* com.nova.spring.entity.Card.test(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("around：方法执行之前！");
        Object val = point.proceed();
        System.out.println("around：方法执行之后！，结果为：" + val);
        return val;
    }
}
