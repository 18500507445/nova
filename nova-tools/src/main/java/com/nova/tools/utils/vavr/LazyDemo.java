package com.nova.tools.utils.vavr;

import io.vavr.Lazy;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 16:28
 */
class LazyDemo {

    public static void main(String[] args) {
        Lazy<String> name = Lazy.of(() -> "Deepak");
        //false
        System.err.println(name.isEvaluated());
        //Deepak
        System.err.println(name.get());
        //True
        System.err.println(name.isEvaluated());
        //Deepak, from cache
        System.err.println(name.get());
    }
}
