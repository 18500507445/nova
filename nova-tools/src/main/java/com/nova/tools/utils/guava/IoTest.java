package com.nova.tools.utils.guava;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 18:24
 */
class IoTest {

    /**
     * 读取指定的文本文件的内容为一个字符串
     */
    @Test
    public void test1() throws IOException {
        String path = "E:\\ideaProjects2\\utils-demo\\src\\test\\resources\\1.txt";
        String string = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        System.err.println(string);
    }

    /**
     * 读取指定的文本文件的内容为List<String>
     */
    @Test
    public void test2() throws IOException {
        String path = "E:\\ideaProjects2\\utils-demo\\src\\test\\resources\\1.txt";
        List<String> list = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);
        System.err.println(list.size());
    }

    /**
     * 获取文件的后缀
     * 获取文件的baseName
     */
    @Test
    public void test3() throws IOException {
        String path = "E:\\ideaProjects2\\utils-demo\\src\\test\\resources\\1.txt";
        // 获取baseName
        System.err.println(FilenameUtils.getBaseName(path));
        // 获取后缀
        System.err.println(FilenameUtils.getExtension(path));
    }

}
