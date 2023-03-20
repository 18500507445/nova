package com.nova.book.jvm.chapter2.section1;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 弱引用演示
 * @author: wzh
 * @date: 2023/3/19 13:48
 */
class Weak {

    private static final int _4MB = 4 * 1024 * 1024;

    public static void main(String[] args) {
        //  list --> WeakReference --> byte[]
        List<WeakReference<byte[]>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WeakReference<byte[]> ref = new WeakReference<>(new byte[_4MB]);
            list.add(ref);
            for (WeakReference<byte[]> w : list) {
                System.out.print(w.get() + " ");
            }
            System.out.println();

        }
        System.out.println("循环结束：" + list.size());
    }
}
