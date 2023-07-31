package com.nova.book.effectivejava.chapter3.section3;

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
        System.err.println("cat = " + cat);
    }

    @Test
    public void demoB() {
        Map<String, Object> map = new HashMap<>(16);
        Cat cat = new Cat(1, map);

        Map<String, Object> classMap = cat.getMap();
        System.err.println("classMap = " + classMap);

        map.put("a", "b");

        System.err.println("classMap = " + classMap);

    }

    @Test
    public void demoC() {
        Cat cat = new Cat(1);
        Map<Integer, Object> initMap = cat.getInitMap();
        initMap.put(1, "a");

        System.err.println("initMap = " + initMap);
    }

    /**
     *
     */
    @Test
    public void demoD() {
        char[] arr = {'a', 'b', 'c'};
        char[] copy = arr;

        copy[1] = 'm';

        System.err.println("arr = " + Arrays.toString(arr));
    }


}

