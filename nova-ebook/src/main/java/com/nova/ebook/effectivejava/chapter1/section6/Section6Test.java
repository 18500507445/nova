package com.nova.ebook.effectivejava.chapter1.section6;


import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 16:52
 */
class Section6Test {

    public static final Map<Integer, Object> MAP = new LinkedHashMap<>();

    static {
        MAP.put(1, "wzh");
        MAP.put(2, new Object());
    }

    /**
     * 静态的集合、map、数组都需要注意
     */
    @Test
    public void demoA() {
        //2不需要了 remove掉
        Object remove = MAP.remove(1);
        System.out.println("remove = " + remove);
    }

    /**
     * 关闭资源：try-with-source优于try-finally
     */
    @Test
    public void demoB() {
        //继承了AutoCloseable 自动关闭资源
        try (FileInputStream fis = new FileInputStream("")) {
            //todo 业务
            System.out.println("读入文件");
        } catch (IOException e) {
            System.out.println("路径错误");
        }
    }

}
