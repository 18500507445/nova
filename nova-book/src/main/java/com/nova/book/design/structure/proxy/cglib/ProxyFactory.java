package com.nova.book.design.structure.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Cglib子类代理工厂
 * @description: 对UserDao在内存中动态构建一个子类对象，因为采用的是继承，所以不能对final修饰的类进行代理
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class ProxyFactory implements MethodInterceptor {

    /**
     * 维护目标对象
     */
    private final Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * 给目标对象创建一个代理对象
     * @return
     */
    public Object getProxyInstance() {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.err.println("Cglib代理：开始事务...");

        //执行目标对象的方法
        Object returnValue = method.invoke(target, objects);

        System.err.println("Cglib代理：提交事务...");

        return returnValue;
    }
}
