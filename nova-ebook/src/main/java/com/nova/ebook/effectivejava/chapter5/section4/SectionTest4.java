package com.nova.ebook.effectivejava.chapter5.section4;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest4 {

    enum Type {
        SPRING,

        SUMMER,

        AUTUMN;
    }

    private final String name;

    private final Type type;

    public SectionTest4(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Test
    public void demoA() {
        EnumMap<Type, Object> typeMap = new EnumMap<>(Type.class);

        for (Type type : typeMap.keySet()) {
            System.out.println(type);
        }

    }


}
