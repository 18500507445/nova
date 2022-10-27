package com.nova.tools.guava;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 18:24
 */
public class IoTest {

    /**
     * 读取指定的文本文件的内容为一个字符串
     */
    @Test
    public void test1() throws IOException {
        String path = "E:\\ideaProjects2\\utils-demo\\src\\test\\resources\\1.txt";
        String string = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        System.out.println(string);
    }

    /**
     * 读取指定的文本文件的内容为List<String>
     */
    @Test
    public void test2() throws IOException {
        String path = "E:\\ideaProjects2\\utils-demo\\src\\test\\resources\\1.txt";
        List<String> list = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);
        System.out.println(list.size());
    }

    /**
     * 获取文件的后缀
     * 获取文件的baseName
     */
    @Test
    public void test3() throws IOException {
        String path = "E:\\ideaProjects2\\utils-demo\\src\\test\\resources\\1.txt";
        // 获取baseName
        System.out.println(FilenameUtils.getBaseName(path));
        // 获取后缀
        System.out.println(FilenameUtils.getExtension(path));
    }

}
