package com.nova.ebook.effectivejava.chapter1.section3;

/**
 * @description: 枚举类创建单例
 * @author: wzh
 * @date: 2023/2/12 21:49
 */
enum Cat {

    INSTANCE;

    private String name;

    private Integer id;

    public void dosh() {

    }

    public void dosh2() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
