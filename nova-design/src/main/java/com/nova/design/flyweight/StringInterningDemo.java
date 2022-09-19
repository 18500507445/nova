package com.nova.design.flyweight;

/**
 * Created by Landy on 2019/1/5.
 */
public class StringInterningDemo {

    public static void main(String[] args) {
        //放在Heap中
        String s = "Hello World";
        //放在常量池中
        String newValue = s.intern();

        System.out.println(newValue);
    }

}
