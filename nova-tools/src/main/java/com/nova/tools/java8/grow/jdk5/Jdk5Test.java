package com.nova.tools.java8.grow.jdk5;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author: wzh
 * @description jdk5测试类
 * @date: 2023/07/21 16:10
 */
public class Jdk5Test {

    @Test
    public void autoBoxing() {
        int a = new Integer(66);
        Integer b = 18;

        Boolean flag = true;
        boolean isBug = Boolean.FALSE;
    }

    @Test
    public void forEach() {
        int[] arr = {1, 4, 5, 7};

        for (int i : arr) {
            System.err.println(i);
        }

        List<String> names = Arrays.asList("王爵nice", "Gay冰", "A*熊");
        for (String name : names) {
            System.err.println(name);
        }
    }

    @Test
    public void staticImport() {
        System.err.println("Hi let learn java 8.");

    }

    @Test
    public void varArgs() {
        List<String> hello = Arrays.asList("王爵nice", "Gay冰", "A*熊");
        System.err.println("hello = " + hello);
    }

}
