package com.nova.search.solr.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author: wzh
 * @description: solr配置类
 * @date: 2024/11/22 10:04
 */
@Getter
@Configuration
public class SolrConfig {

    @Value("#{${spring.data.solr.collectionMap:null}}")
    private Map<String, String> collectionMap;

}
