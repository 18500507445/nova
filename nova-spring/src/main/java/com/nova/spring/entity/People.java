package com.nova.spring.entity;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/29 15:25
 */
public class People {

    private String name;

    private Integer age;

    /**
     * 分别在say方法执行前后切入
     *
     * @param str
     * @return
     */
    public int say(String str) {
        System.out.println("我想说：" + str);
        return str.length();
    }

    @Deprecated
    public void test(){
        System.out.println("我是过时方法!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
