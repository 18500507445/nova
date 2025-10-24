package com.nova.starter.mongo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/22 20:53
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.data.mongodb.core.MongoTemplate")
public class MongoAutoConfiguration {

    @Resource
    private ApplicationContext applicationContext;

    @Bean(name = "mongoService")
    public MongoService mongoService() {
        return new MongoService();
    }

    /**
     * 第一数据源配置信息
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public MongoProperties primaryMongoProperties() {
        return new MongoProperties();
    }

    /**
     * 默认 MongoTemplate
     * ConditionalOnBean 只有当名为primaryMongoProperties的Bean存在于容器中时，才会创建和配置primaryMongoTemplate的bean
     */
    @Bean(name = {"primaryMongoTemplate", "mongoTemplate"})
    @Primary
    @ConditionalOnBean(name = "primaryMongoProperties")
    public MongoTemplate primaryMongoTemplate(MappingMongoConverter mappingMongoConverter) throws UnsupportedEncodingException {
        MongoProperties mongoProperties = this.primaryMongoProperties();
        if (null != mongoProperties.getHost()) {
            MongoTemplate mongoTemplate = new MongoTemplate(primaryMongoFactory(mongoProperties), mappingMongoConverter);
            MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
            mongoMapping.afterPropertiesSet();
            return mongoTemplate;
        } else {
            return null;
        }
    }

    /**
     * 调用 mongoTemplate 的save方法时
     * spring-data-mongodb 的 TypeConverter 会自动给 Document 添加一个 _class
     * spring-data-mongodb 是为了在把 Document 转换成 Java 对象时能够转换到具体的子类
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setAutoIndexCreation(true);
        mappingContext.setApplicationContext(applicationContext);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingMongoConverter;
    }

    /**
     * 默认 MongoDatabaseFactory
     * url = 'mongodb://userName:password@ip:port/database?authSource=authentication-database'
     */
    @Bean
    @Primary
    @ConditionalOnBean(name = "primaryMongoProperties")
    public MongoDatabaseFactory primaryMongoFactory(MongoProperties mongoProperties) throws UnsupportedEncodingException {
        String url;
        if (null != mongoProperties.getUri()) {
            url = mongoProperties.getUri();
        } else {
            StringBuilder uri = new StringBuilder("mongodb://");
            if (null != mongoProperties.getUsername()) {
                uri.append(mongoProperties.getUsername());
                uri.append(":");
            }
            if (null != mongoProperties.getPassword()) {
                String password = Arrays.toString(mongoProperties.getPassword());
                password = password.replace("[", "").replace("]", "").replace(", ", "");
                uri.append(URLEncoder.encode(password, "utf-8"));
                uri.append("@");
            }
            uri.append(mongoProperties.getHost());
            uri.append(":");
            uri.append(mongoProperties.getPort());
            if (null != mongoProperties.getDatabase()) {
                uri.append("/").append(mongoProperties.getDatabase());
            }
            if (null != mongoProperties.getAuthenticationDatabase()) {
                uri.append("?authSource=").append(mongoProperties.getAuthenticationDatabase());
            }
            url = uri.toString();
        }
        return new SimpleMongoClientDatabaseFactory(url);
    }

}
