package com.nova.book.effectivejava.chapter1.section1;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 其实就是单例
 * @author: wzh
 * @date: 2023/2/12 14:49
 */
@Data
class Cat {

    private Integer id;

    private String name;

    /**
     * 私有构造，也就是对外不让new Cat()了
     */
    private Cat() {

    }

    private Cat(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private static Cat cat = new Cat();

    private static Map<String, Cat> catMap = new HashMap<>();

    public static Cat getSingleton() {
        return cat;
    }

    /**
     * 判断一个map中是否存在这个key，如果存在则处理value的数据，
     * 如果不存在，则创建一个满足value要求的数据结构放到value中。
     *
     * @param key
     * @return
     */
    public static Cat getNotRepeat(String key) {
        return catMap.computeIfAbsent(key, s -> new Cat(1, s));
    }

}


