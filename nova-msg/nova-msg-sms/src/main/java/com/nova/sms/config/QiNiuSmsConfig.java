package com.nova.sms.config;

import com.nova.sms.service.impl.QiNiuSmsTemplate;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 七牛云短信配置类
 * @author: wzh
 * @date: 2023/4/20 19:51
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnClass(SmsManager.class)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.name", havingValue = "qiniu")
public class QiNiuSmsConfig {

    @Bean
    public QiNiuSmsTemplate qiNiuSmsTemplate(SmsProperties smsProperties) {
        Auth auth = Auth.create(smsProperties.getAccessKey(), smsProperties.getSecretKey());
        SmsManager smsManager = new SmsManager(auth);
        return new QiNiuSmsTemplate(smsProperties, smsManager);
    }
}

