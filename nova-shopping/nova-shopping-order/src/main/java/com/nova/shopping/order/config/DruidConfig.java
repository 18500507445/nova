package com.nova.shopping.order.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.nova.shopping.common.config.database.DruidProperties;
import com.nova.shopping.common.config.database.DynamicDataSource;
import com.nova.shopping.common.enums.DataSourceType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * druid 配置多数据源
 *
 * @author: my-pay
 */
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(16);
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
        setDataSource(targetDataSources, DataSourceType.SLAVE.name(), "slaveDataSource");
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

    /**
     * 设置数据源
     *
     * @param targetDataSources 备选数据源集合
     * @param sourceName        数据源名称
     * @param beanName          bean名称
     */
    public void setDataSource(Map<Object, Object> targetDataSources, String sourceName, String beanName) {
        try {
            DataSource dataSource = SpringUtil.getBean(beanName);
            targetDataSources.put(sourceName, dataSource);
        } catch (Exception ignored) {

        }
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
