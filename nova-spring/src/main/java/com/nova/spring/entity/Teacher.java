package com.nova.spring.entity;

import lombok.ToString;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/29 15:00
 */
@ToString
public class Teacher {

    private String name;

    private Integer age;

    public Teacher(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
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
}
