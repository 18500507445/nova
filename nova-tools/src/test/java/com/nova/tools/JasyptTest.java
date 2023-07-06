package com.nova.tools;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: wzh
 * @description 配置文件加解密测试
 * @date: 2023/05/30 15:36
 */
@SpringBootTest
public class JasyptTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    /**
     * 加密解密测试
     */
    @Test
    public void jasyptTest() {
        // 加密
        System.out.println(stringEncryptor.encrypt("root"));
        // 解密
        System.out.println(stringEncryptor.decrypt("4+fSPJL3wq+pZm9IVnD9ssbuH0qW1vky4Kdq0EO5vOe1LdTl1+DpnjrXImMb5ef5"));
    }

}
