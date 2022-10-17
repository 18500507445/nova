package com.nova.mail;

import com.nova.mail.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/9/3 20:37
 */
@SpringBootTest
public class MailApplicationTest {

    @Autowired
    private MailService mailService;

    public static void main(String[] args) {

    }

    @Test
    public void testMqMessage() {
        mailService.sendSimpleMail("18500507445@163.com", "nova测试邮件", "你好啊");
    }
}
