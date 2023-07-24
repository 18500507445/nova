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

    /**
     * 读取properties文件属性
     */
    @Test
    public void readProperties() {
        String test = PropertiesUtils.getInstance().getPropertyValue("test", "name");
        System.out.println("test = " + test);
    }

    /**
     * 读取yml文件属性
     */
    @Test
    public void readYmlFile() {
        String port = env.getProperty("server.port");
        System.out.println("port = " + port);
    }

    @Test
    public void readValue() {
        System.out.println("path = " + path);
    }
}
