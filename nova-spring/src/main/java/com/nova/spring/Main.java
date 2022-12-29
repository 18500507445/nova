package com.nova.spring;

import com.nova.spring.config.MainConfiguration;
import com.nova.spring.config.TestConfiguration;
import com.nova.spring.entity.*;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/29 13:44
 */
public class Main {

    /**
     * 测试IoC 控制反转
     */
    @Test
    public void testIoC() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");

        Student student = context.getBean(Student.class);
        Student student2 = (Student) context.getBean("student");

        System.out.println("student = " + student);
        System.out.println("student2 = " + student2);

        //手动销毁容器
        context.close();
    }

    /**
     * 测试依赖注入
     */
    @Test
    public void testDi() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
        Student student = context.getBean(Student.class);
        System.out.println("student=" + student);
        student.say();
    }

    /**
     * autowire自动注入和指定构造方法
     * <p>
     * autowire="byType" 根据类型自动注入，不需要写property了
     */
    @Test
    public void testAutowire() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
        Teacher teacher = context.getBean(Teacher.class);
        System.out.println("teacher = " + teacher);
    }

    /**
     * 测试Aop before(前置)，after(后置)，around(环绕)
     * 通过CGLIB第三方库
     */
    @Test
    public void testAop() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-beans.xml");
        People people = context.getBean(People.class);

        System.out.println("people.getClass() = " + people.getClass());
        people.say("卢本伟牛皮");
        //people.test();

    }

    /**
     * 测试注解装配bean
     * 默认单例模式 原型模式 @Scope("prototype")
     * 单例模式 创建content之后，然后new Card()交给容器，每次去getBean获取的都是同一个对象;
     * 原型模式 每次getBean去new Card();
     */
    @Test
    public void testAnnotation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfiguration.class);
        Card card = context.getBean(Card.class);

        System.out.println("card = " + card);

        People people = context.getBean(People.class);
        People people2 = context.getBean(People.class);
        System.out.println("people = " + people);
        System.out.println("people2 = " + people2);
    }

    /**
     * 测试扫描装配bean
     */
    @Test
    public void testScanAnnotation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfiguration.class);
        CardScan cardScan = context.getBean(CardScan.class);
        System.out.println("cardScan = " + cardScan);
    }


    /**
     * 配置注解实现AOP操作
     * EnableAspectJAutoProxy
     */
    @Test
    public void testAnnotationAop() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfiguration.class);
        Card card = context.getBean(Card.class);
        card.test("卢本伟牛皮");
    }

    /**
     * 测试import注解导入bean
     */
    @Test
    public void testImportConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);
    }


}
