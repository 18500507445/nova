package com.nova.design.create.singleton;

import com.nova.design.create.singleton.enums.EnumClass;
import com.nova.design.create.singleton.hungry.Hungry;
import com.nova.design.create.singleton.inner.InnerClass;
import com.nova.design.create.singleton.lazy.DoubleRetrieval;
import com.nova.design.create.singleton.lazy.LazyNoSafe;
import com.nova.design.create.singleton.lazy.LazySafe;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;


/**
 * @description: 单利模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    /**
     * 恶汉式
     */
    @Test
    public void hungryTest() throws Exception {
        Hungry instance1 = Hungry.getInstance();
        Hungry instance2 = Hungry.getInstance();
        System.out.println(instance2 == instance1);

        //反射 破坏了单例
        Constructor<LazySafe> declaredConstructor = LazySafe.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        LazySafe instance3 = declaredConstructor.newInstance();
        LazySafe instance4 = declaredConstructor.newInstance();

        System.out.println(instance4 == instance3);
    }

    /**
     * 懒汉线程不安全
     */
    @Test
    public void lazyNoSafeTest() {
        LazyNoSafe instance1 = LazyNoSafe.getInstance();
        LazyNoSafe instance2 = LazyNoSafe.getInstance();
        System.out.println(instance2 == instance1);
    }

    /**
     * 懒汉线程安全
     */
    @Test
    public void lazySafeTest() {
        LazySafe instance1 = LazySafe.getInstance();
        LazySafe instance2 = LazySafe.getInstance();
        System.out.println(instance2 == instance1);
    }

    /**
     * 双重锁
     */
    @Test
    public void doubleRetrievalTest() {
        DoubleRetrieval instance1 = DoubleRetrieval.getInstance();
        DoubleRetrieval instance2 = DoubleRetrieval.getInstance();
        System.out.println(instance2 == instance1);
    }

    /**
     * 静态内部类
     */
    @Test
    public void innerClassTest() {
        InnerClass instance1 = InnerClass.getInstance();
        InnerClass instance2 = InnerClass.getInstance();
        System.out.println(instance2 == instance1);
    }

    /**
     * 枚举
     */
    @Test
    public void enumTest() {
        EnumClass instance1 = EnumClass.INSTANCE;
        EnumClass instance2 = EnumClass.INSTANCE;
        System.out.println(instance2 == instance1);
    }

}