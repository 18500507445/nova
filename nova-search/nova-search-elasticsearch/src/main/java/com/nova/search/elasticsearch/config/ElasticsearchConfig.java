package com.nova.search.elasticsearch.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.util.List;

/**
 * @author: wzh 过时方法可以不用看了
 * @description: 配置类，继承AbstractElasticsearchConfiguration，实现elasticsearchClient()抽象方法，创建RestHighLevelClient对象。
 * @date: 2023/09/22 10:44
 */
//@ConfigurationProperties(prefix = "spring.elasticsearch")
//@Configuration
@Data
@Deprecated
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private List<String> uris;

    /**
     * 设置为static方便后面调用
     */
    public static RestHighLevelClient restHighLevelClient;

    @NotNull
    @Override
    public RestHighLevelClient elasticsearchClient() {
        String[] split = uris.get(0).split(":");
        RestClientBuilder builder = RestClient.builder(new HttpHost(split[0], Integer.parseInt(split[1])));
        restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }
}
