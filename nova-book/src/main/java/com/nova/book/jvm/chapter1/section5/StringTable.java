package com.nova.book.jvm.chapter1.section5;

/**
 * @description: StringTable ["a","b","ab"]
 * @author: wzh
 * @date: 2023/3/17 17:52
 */
class StringTable {

    /**
     * 常量池中的信息，都会被加载到运行时常量池中，这时a，b，ab都是常量池中的符号，还没有变为java字符串对象
     * 1dc #2 会把a符号变为"a"字符串对象
     *
     * @param args
     */
    public static void main(String[] args) {
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";

        //new StringBuilder.append("a").append("b").toString 这个是在堆内存中
        String s4 = s1 + s2;

        System.out.println(s3 == s4);

        //这里就是直接找常量池中已经存在的"ab"固相等
        String s5 = "a" + "b";
        System.out.println(s3 == s5);

    }
}
