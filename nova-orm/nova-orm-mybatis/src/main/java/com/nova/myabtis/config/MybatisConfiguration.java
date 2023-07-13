package com.nova.myabtis.config;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @description: 配置类
 * Configuration 配置注解
 * MapperScan 扫描mapper
 * ComponentScan 扫描service
 * EnableTransactionManagement 开启事务
 * EnableAspectJAutoProxy 开启aop
 * @author: wzh
 * @date: 2022/12/31 20:34
 */
@Configuration
@MapperScan("com.nova.mybatis.mapper")
@ComponentScan("com.nova.mybatis.service")
@EnableTransactionManagement
public class MybatisConfiguration {

    /**
     * 方式一：读取xml配置
     *
     * @return
     * @throws IOException
     */
    //@Bean
    //public SqlSessionTemplate sqlSessionTemplate() throws IOException {
    //    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
    //    return new SqlSessionTemplate(factory);
    //}

    /**
     * 方式二：DataSource注入bean配置数据源连接
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        return new PooledDataSource("com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://47.100.174.176:3306/study", "root", "@wangzehui123");
    }

    /**
     * 放入SqlSessionFactoryBean中
     *
     * @param dataSource
     * @return
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }

    /**
     * 事物管理器 注入bean
     *
     * @param dataSource
     * @return
     */
    @Bean
    public TransactionManager transactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
