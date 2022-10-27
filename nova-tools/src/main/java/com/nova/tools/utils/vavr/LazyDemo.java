package com.nova.tools.utils.vavr;

import io.vavr.Lazy;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 16:28
 */
public class LazyDemo {

    public static void main(String[] args) {
        Lazy<String> name = Lazy.of(() -> "Deepak");
        //false
        System.out.println(name.isEvaluated());
        //Deepak
        System.out.println(name.get());
        //True
        System.out.println(name.isEvaluated());
        //Deepak, from cache
        System.out.println(name.get());
    }
}
