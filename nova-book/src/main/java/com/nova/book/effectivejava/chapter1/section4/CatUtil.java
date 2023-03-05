package com.nova.book.effectivejava.chapter1.section4;

/**
 * @description: 工具类私有构造，对外只提供静态方法
 * @author: wzh
 * @date: 2023/2/13 16:47
 */
public class CatUtil {

    private CatUtil() {

    }

    public static boolean isCat(String name) {
        return "tom".equals(name);
    }
}
