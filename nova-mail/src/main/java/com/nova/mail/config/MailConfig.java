package com.nova.mail.config;

import com.nova.mail.service.MailService;
import com.nova.mail.service.impl.MailServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/9/3 19:43
 */
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailConfig {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private MailProperties mailProperties;

    @Bean
    @ConditionalOnBean({MailProperties.class, JavaMailSender.class})
    public MailService mailService() {
        return new MailServiceImpl(mailSender, mailProperties);
    }
}
