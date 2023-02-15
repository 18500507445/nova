package com.nova.ebook.effectivejava.chapter3.section3;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/15 11:11
 */
class Section3Test {

    @Test
    public void demoA() {
        Cat cat = new Cat(1);
        System.out.println("cat = " + cat);
    }

    @Test
    public void demoB() {
        Map<String, Object> map = new HashMap<>(16);
        Cat cat = new Cat(1, map);

        Map<String, Object> classMap = cat.getMap();
        System.out.println("classMap = " + classMap);

        map.put("a", "b");

        System.out.println("classMap = " + classMap);

    }

    @Test
    public void demoC() {
        Cat cat = new Cat(1);
        Map<Integer, Object> initMap = cat.getInitMap();
        initMap.put(1, "a");

        System.out.println("initMap = " + initMap);
    }

    /**
     *
     */
    @Test
    public void demoD() {
        char[] arr = {'a', 'b', 'c'};
        char[] copy = arr;

        copy[1] = 'm';

        System.out.println("arr = " + Arrays.toString(arr));
    }


}

