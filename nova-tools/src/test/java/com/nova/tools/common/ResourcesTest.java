package com.nova.tools.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.nova.common.utils.spring.PropertiesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.List;

/**
 * @author: wzh
 * @description 读取文件测试类
 * @date: 2023/07/24 14:42
 */
@SpringBootTest
public class ResourcesTest {

    @Autowired
    private Environment env;

    @Value("${server.servlet.context-path}")
    private String path;

    /**
     * 读取properties文件属性
     */
    @Test
    public void readProperties() {
        String test = PropertiesUtils.getInstance().getPropertyValue("test", "name");
        System.err.println("test = " + test);
    }

    /**
     * 读取yml文件属性
     */
    @Test
    public void readYmlFile() {
        String port = env.getProperty("server.port");
        System.err.println("port = " + port);
    }

    /**
     * 注入方式读取属性值
     */
    @Test
    public void readValue() {
        System.err.println("path = " + path);
    }

    //@Value读取配置，给变量赋值
    private static Integer PORT = 123;
    @Value("${server.port:123}")
    public void setPort(Integer port) {
        PORT = port;
    }

    @Test
    public void staticValue() {
        System.out.println("PORT = " + PORT);
    }

    /**
     * 文件内容对比
     */
    @Test
    public void fileContrast() {
        ClassPathResource resource1 = new ClassPathResource("1.json");
        ClassPathResource resource2 = new ClassPathResource("2.json");
        List<String> list1 = StrUtil.split(resource1.readUtf8Str(), "\n");
        List<String> list2 = StrUtil.split(resource2.readUtf8Str(), "\n");

        Collection<String> subtract1 = CollUtil.subtract(list1, list2);
        System.out.println(subtract1);
    }

}
