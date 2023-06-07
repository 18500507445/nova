package com.starter.xxl;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @description: xxl-job自动装配配置类
 * @author: wzh
 * @date: 2023/4/22 22:12
 */
@AutoConfiguration
@EnableConfigurationProperties({XxlProperties.class})
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
