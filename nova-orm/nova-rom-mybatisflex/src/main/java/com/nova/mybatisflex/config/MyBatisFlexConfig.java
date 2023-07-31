package com.nova.mybatisflex.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wzh
 * @description 配置类
 * @date: 2023/07/31 16:16
 */
@Configuration
@Slf4j
public class MyBatisFlexConfig implements ConfigurationCustomizer, SqlSessionFactoryBeanCustomizer, MyBatisFlexCustomizer {

    @Override
    public void customize(FlexConfiguration flexConfiguration) {
        log.warn("flexConfiguration 配置！");
        //开启审计功能
        AuditManager.setAuditEnable(true);

        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("{}, 耗时：{} ms", auditMessage.getFullSql(), auditMessage.getElapsedTime())
        );
    }

    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        log.warn("MybatisConfiguration 配置！");
    }

    @Override
    public void customize(SqlSessionFactoryBean sqlSessionFactoryBean) {
        log.warn("SqlSessionFactoryBean 配置！");
    }
}
