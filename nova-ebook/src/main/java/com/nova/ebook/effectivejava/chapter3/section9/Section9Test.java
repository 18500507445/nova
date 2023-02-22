package com.nova.ebook.effectivejava.chapter3.section9;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 20:58
 */
class Section9Test {

    public static final List<Cat> CATS = new ArrayList<>(Arrays.asList(new Cat(1, "tom", 18), new Cat(2, "kitty", 15)));

    /**
     * 最开始需求
     */
    @Test
    public void demoA() {
        Comparator<Cat> idComparator = new Comparator<Cat>() {
            @Override
            public int compare(Cat o1, Cat o2) {
                return o1.getId() - o2.getId();
            }
        };
        CATS.sort(idComparator);
        CATS.forEach(System.out::println);

        System.out.println("-----------------------------");

        Comparator<Cat> ageComparator = new Comparator<Cat>() {
            @Override
            public int compare(Cat o1, Cat o2) {
                return o1.getAge() - o2.getAge();
            }
        };
        CATS.sort(ageComparator);
        CATS.forEach(System.out::println);
    }

    /**
     * 函数对象表示策略
     */
    @Test
    public void demoB() {
        CATS.sort(CatComparator.idComparator());
        CATS.forEach(System.out::println);

        System.out.println("-----------------------------");
        CATS.sort(CatComparator.ageComparator());
        CATS.forEach(System.out::println);
    }

    /**
     * jdk8之后
     */
    @Test
    public void demoC() {
        CATS.sort(Comparator.comparing(Cat::getId));
        CATS.forEach(System.out::println);

        System.out.println("-----------------------------");

        CATS.sort(Comparator.comparing(Cat::getAge));
        CATS.forEach(System.out::println);
    }


}
