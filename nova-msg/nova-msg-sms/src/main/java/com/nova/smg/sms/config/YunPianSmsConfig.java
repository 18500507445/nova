package com.nova.smg.sms.config;

import com.nova.smg.sms.service.impl.YunPianSmsTemplate;
import com.yunpian.sdk.YunpianClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/20 20:15
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnClass(YunpianClient.class)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.name", havingValue = "yunpian")
public class YunPianSmsConfig {

    @Bean
    public YunPianSmsTemplate yunPianSmsTemplate(SmsProperties smsProperties) {
        YunpianClient client = new YunpianClient(smsProperties.getAccessKey()).init();
        return new YunPianSmsTemplate(smsProperties, client);
    }
}
