package com.nova.starter.xxl;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: xxl-job自动装配配置类
 * @author: wzh
 * @date: 2023/4/22 22:12
 */
@Configuration
//指定了XxlProperties为一个属性类
@EnableConfigurationProperties({XxlProperties.class})
@ConditionalOnClass(name = "com.nova.starter.xxl.XxlProperties")
public class XxlAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public XxlJobExecutor xxlJobExecutor(XxlProperties properties) {
        XxlProperties.AdminProperties admin = properties.getAdmin();
        XxlProperties.ExecutorProperties executor = properties.getExecutor();
        if (null != admin && null != executor) {
            // 初始化执行器
            XxlJobExecutor xxlJobExecutor = new XxlJobSpringExecutor();
            xxlJobExecutor.setIp(executor.getIp());
            xxlJobExecutor.setPort(executor.getPort());
            xxlJobExecutor.setAppname(executor.getAppName());
            xxlJobExecutor.setLogPath(executor.getLogPath());
            xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
            xxlJobExecutor.setAdminAddresses(admin.getAddresses());
            xxlJobExecutor.setAccessToken(properties.getAccessToken());
            return xxlJobExecutor;
        }
        return null;
    }
}
