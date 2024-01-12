package com.nova.book.effectivejava.chapter2.section4;

import lombok.Data;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:46
 */
@Data
class Cat implements Cloneable {

    private Integer id;

    private String name;

    private String colour;

    public Cat() {
        System.err.println("Cat 无参构造器");
    }

    public Cat(Integer id, String name, String colour) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        System.err.println("Cat 有参构造器");
    }

    @Override
    protected Cat clone() throws CloneNotSupportedException {
        System.err.println("clone Cat");
        return (Cat) super.clone();
    }
}
