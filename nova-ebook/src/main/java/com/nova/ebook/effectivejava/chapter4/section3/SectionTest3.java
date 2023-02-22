package com.nova.ebook.effectivejava.chapter4.section3;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest3 {

    /**
     * 数组 编译正确，运行报错
     */
    @Test
    public void demoA(){
        Object[] arr = new String[3];
        arr[0] = 1L;
    }

    /**
     * list 编译就报错
     */
    @Test
    public void demoB(){
        //List<String> list = new ArrayList<Object>();
    }

}
