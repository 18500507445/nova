package com.nova.mail;

import com.nova.mail.service.MailService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: wzh
 * @date: 2022/9/3 20:37
 */
@SpringBootTest
public class MailApplicationTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private StringEncryptor stringEncryptor;

    public static void main(String[] args) {

    }

    @Test
    public void testMqMessage() {
        mailService.sendSimpleMail("18500507445@163.com", "nova测试邮件", "你好啊");
    }

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
