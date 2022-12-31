package com.nova.spring.entity;

import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/29 13:46
 */
@ToString
public class Student {

    private String name;

    private Integer age;

    private Card card;

    private List<String> list;

    private Map<String,Integer> score;

    private void init() {
        System.out.println("我是初始化方法！");
    }

    private void destroy() {
        System.out.println("我是销毁方法！");
    }

    public Student() {
        System.out.println("我是student");
    }

    public void say() {
        System.out.println("我是：" + this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, Integer> getScore() {
        return score;
    }

    public void setScore(Map<String, Integer> score) {
        this.score = score;
    }
}
