package com.nova.ebook.effectivejava.chapter2.section5;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 20:20
 */
class Section5Test {

    public static List<Cat> cats = new ArrayList<>(Arrays.asList(new Cat(2), new Cat(1)));

    /**
     * 实现Comparable接口的排序
     */
    @Test
    public void demoA() {
        cats.sort(Cat::compareTo);

        System.out.println(JSONUtil.toJsonStr(cats));
    }

    /**
     * java8排序
     */
    @Test
    public void demoB() {
        cats.sort(Comparator.comparingInt(Cat::getId));

        System.out.println(JSONUtil.toJsonStr(cats));
    }


}
