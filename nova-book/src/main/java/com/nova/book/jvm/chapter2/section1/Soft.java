package com.nova.book.jvm.chapter2.section1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 软引用
 * -Xmx20m
 * @author: wzh
 * @date: 2023/3/18 08:59
 */
class Soft {

    private static final int _4MB = 4 * 1024 * 1024;

    @Test
    public void demoA() throws IOException {
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new byte[_4MB]);
        }
        System.in.read();
    }

    @Test
    public void demoB() throws IOException {
        List<SoftReference<byte[]>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SoftReference<byte[]> ref = new SoftReference<>(new byte[_4MB]);
            System.out.println(Arrays.toString(ref.get()));
            list.add(ref);
            System.out.println(list.size());

        }
        System.out.println("循环结束：" + list.size());
        for (SoftReference<byte[]> ref : list) {
            System.out.println(ref.get());
        }
    }


}
