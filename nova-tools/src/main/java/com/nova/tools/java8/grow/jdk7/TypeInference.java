package com.nova.tools.java8.grow.jdk7;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型推断
 *
 * @author wzh
 * @date 2018/2/8
 */
public class TypeInference {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");

        List<? extends String> list2 = new ArrayList<>();
        list.addAll(list2);
    }
}
