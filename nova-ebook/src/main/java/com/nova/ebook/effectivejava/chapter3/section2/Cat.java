package com.nova.ebook.effectivejava.chapter3.section2;

import cn.hutool.core.util.StrUtil;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 11:37
 */
class Cat {

    private Integer id;

    private String name;

    public void setName(String name) {
        if (StrUtil.isBlank(name)) {
            throw new RuntimeException("Cat.name不能为空或null");
        }
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        if (StrUtil.isBlank(name)) {
            name = "tom";
        }
        return name;
    }
}
