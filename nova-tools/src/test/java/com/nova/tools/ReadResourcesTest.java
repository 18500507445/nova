package com.nova.tools;

import com.nova.common.utils.spring.PropertiesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * @author: wzh
 * @description 读取文件测试类
 * @date: 2023/07/24 14:42
 */
@SpringBootTest
public class ReadResourcesTest {

    @Autowired
    private Environment env;

    @Value("${server.servlet.context-path}")
    private String path;

    public static String STATIC_PATH;

    @Value("${server.servlet.context-path}")
    public void setStaticPath(String staticPath) {
        STATIC_PATH = staticPath;
    }

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

    /**
     * 属性值赋值静态变量
     * 当前类需要注册成组件需要有@Component才可以拿到
     */
    @Test
    public void readStaticValue() {
        System.err.println("staticPath = " + ReadResourcesTest.STATIC_PATH);
    }

}
