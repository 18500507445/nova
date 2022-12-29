package com.nova.spring;

import com.nova.spring.entity.People;
import com.nova.spring.entity.Student;
import com.nova.spring.entity.Teacher;
import org.junit.Test;
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

}
