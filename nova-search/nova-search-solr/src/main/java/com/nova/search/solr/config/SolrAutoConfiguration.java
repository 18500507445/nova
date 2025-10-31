package com.nova.search.solr.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.solr.SolrProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: solr配置类
 * @date: 2025/10/31 09:25
 */
@Slf4j(topic = "nova-search-solr ==> SolrAutoConfiguration")
@Configuration
@EnableConfigurationProperties({SolrProperties.class})
public class SolrAutoConfiguration {

    @Resource
    private SolrProperties solrProperties;

    @Resource
    private SolrConfig solrConfig;

    @Bean
    public SolrClient solrClient() {
        if (ObjectUtil.hasEmpty(solrProperties, solrProperties.getHost(), solrConfig.getCollectionMap())) {
            return null;
        }
        log.warn("装配【SolrClient】当前文档集合：{}", JSONObject.toJSONString(solrConfig.getCollectionMap()));
        return new HttpSolrClient.Builder(solrProperties.getHost()).build();
    }
}
