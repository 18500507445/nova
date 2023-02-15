package com.nova.ebook.effectivejava.chapter3.section3;


import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 18:09
 */
@Getter
@ToString
final class Cat {

    private final Integer id;

    private Map<String, Object> map;

    /**
     * 内部初始化，对外提供一个get方法
     */
    private final Map<Integer, Object> initMap = new HashMap<>();

    public Cat(Integer id) {
        this.id = id;
    }

    public Cat(Integer id, Map<String, Object> map) {
        this.id = id;
        this.map = map;
    }

}
