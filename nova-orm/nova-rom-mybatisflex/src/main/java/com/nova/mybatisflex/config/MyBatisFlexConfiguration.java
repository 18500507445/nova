package com.nova.mybatisflex.config;

import com.mybatisflex.core.audit.AuditManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description 配置类
 * @date: 2023/07/30 14:28
 */
@Configuration
@Slf4j
public class MyBatisFlexConfiguration {

    public MyBatisFlexConfiguration() {
        //开启审计功能
        AuditManager.setAuditEnable(true);

        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("{},{}ms", auditMessage.getFullSql(), auditMessage.getElapsedTime())
        );
    }
}
