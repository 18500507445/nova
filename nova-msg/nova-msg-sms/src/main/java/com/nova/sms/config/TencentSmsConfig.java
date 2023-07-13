package com.nova.sms.config;

import com.nova.sms.service.impl.TencentSmsTemplate;
import com.tencentcloudapi.common.Credential;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 腾讯云短信配置
 * @author: wzh
 * @date: 2023/4/20 20:24
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnClass(Credential.class)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.name", havingValue = "tencent")
public class TencentSmsConfig {

    @Bean
    public TencentSmsTemplate tencentSmsTemplate(SmsProperties smsProperties) {
        Credential credential = new Credential(smsProperties.getSecretId(), smsProperties.getSecretKey());
        return new TencentSmsTemplate(smsProperties, credential);
    }
}
