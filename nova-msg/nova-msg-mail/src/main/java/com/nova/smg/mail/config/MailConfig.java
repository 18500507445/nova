package com.nova.smg.mail.config;

import com.nova.smg.mail.service.MailService;
import com.nova.smg.mail.service.impl.MailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @description: 邮件配置类，加载send和配置类
 * @author: wzh
 * @date: 2022/9/3 19:43
 */
@AllArgsConstructor
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailConfig {

    private final JavaMailSender mailSender;

    /**
     * 注入官方配置类
     */
    private final MailProperties mailProperties;

    @Bean
    @ConditionalOnBean({MailProperties.class, JavaMailSender.class})
    public MailService mailService() {
        return new MailServiceImpl(mailSender, mailProperties);
    }
}
