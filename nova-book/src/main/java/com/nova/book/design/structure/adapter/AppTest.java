package com.nova.book.design.structure.adapter;

import org.junit.jupiter.api.Test;


/**
 * @description: 适配器模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        // 类适配器
        ClassAdapter adapter = new ClassAdapter();
        adapter.usb();
    }

    @Test
    public void test2() {
        // 对象适配器
        ObjAdapter adapter = new ObjAdapter(new TypeCAdapter());
        adapter.usb();
    }

}