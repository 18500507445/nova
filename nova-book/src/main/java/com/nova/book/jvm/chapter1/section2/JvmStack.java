package com.nova.book.jvm.chapter1.section2;

/**
 * @description: 虚拟机栈演示
 * @author: wzh
 * @date: 2023/3/17 09:54
 */
class JvmStack {

    /**
     * debug开启，可以看到main方法先进栈
     * @param args
     */
    public static void main(String[] args) {
        method1();
    }

    /**
     * method1第二个进栈
     */
    private static void method1() {
        method2(1, 2);
    }


    /**
     * method2最后进栈，可以看到有参数和局部变量，记录一个返回地址
     * @param a
     * @param b
     * @return
     */
    private static int method2(int a, int b) {
        int c = a + b;
        return c;
    }
}
