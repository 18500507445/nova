package com.nova.ebook.effectivejava.chapter2.section5;

import lombok.Data;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 20:13
 */
@Data
class Cat implements Comparable {

    private Integer id;

    private String name;

    private String colour;

    public Cat() {

    }

    public Cat(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(Object o) {
        return this.id - ((Cat) o).getId();
    }
}
