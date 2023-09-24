package com.nova.elasticsearch;

import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.nova.elasticsearch.entity.User;
import com.nova.elasticsearch.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author: wzh
 * @description 测试类
 * @date: 2023/07/13 23:00
 */
@SpringBootTest
@Slf4j
public class ElasticsearchTest {

    @Resource
    private UserService userService;

    @Test
    public void insertTest() {
        User user = new User();
        user.setId("1");
        user.setUsername("张三");
        user.setPassword("password");
        userService.save(user);
    }

    @Test
    public void findByIdTest() {
        Optional<User> entity = userService.findById("1");
        log.info("查询成功：{}", JSONUtil.toJsonStr(entity));
    }

    @Test
    public void getAllTest() {
        Iterable<User> iterable = userService.getAll();
        for (User user : iterable) {
            System.err.println("jsonUser = " + JSONUtil.toJsonStr(user));
        }
    }

    @Test
    public void deleteTest() {
        User user = new User();
        user.setId("1");
        userService.delete(user);
    }


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @SneakyThrows
    @Test
    public void queryByRestHighLevelClient() {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("username", "张三"));
        searchRequest.source(searchSourceBuilder);

        // 设置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title"); // 高亮显示的字段
        highlightBuilder.preTags("<em>"); // 高亮显示的起始标签
        highlightBuilder.postTags("</em>"); // 高亮显示的结束标签
        searchSourceBuilder.highlighter(highlightBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.info("client 查询: {}", searchResponse.toString());
    }

    @Test
    public void queryByRestClient() throws IOException {
        // 创建低级客户端
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
        // 使用Jackson映射器创建传输层
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // 创建API客户端
        ElasticsearchClient client = new ElasticsearchClient(transport);

        String indexName = "user";

        //删除
        DeleteResponse delete = client.delete(e -> e.index(indexName).id("1"));
        System.out.println(delete.result());

        //查询
        GetResponse<User> query = client.get(e -> e.index(indexName).id("1"), User.class);
        System.out.println(query.source().toString());

        //修改
        Map<String, Object> map = new HashMap<>();
        map.put("username", "username");
        UpdateResponse<User> update = client.update(e -> e.index(indexName).id("1").doc(map), User.class);
        System.out.println(update.result());

        //创建
        User user = new User();
        CreateResponse create = client.create(e -> e.index(indexName).id("1").document(user));
        System.out.println(create.result());

        //批量添加
        List<BulkOperation> list = new ArrayList<>();
        user.setPassword("password");
        user.setUsername("username");
        list.add(new BulkOperation.Builder()
                //删除替换delete
                .create(d -> d.document(user).id("1").index(indexName))
                .build());
        BulkResponse bulkResponse = client.bulk(e -> e.index(indexName).operations(list));
        System.out.println(bulkResponse.toString());

        // 关闭ES客户端
        transport.close();
        restClient.close();
    }


    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void getListTest() {
        PageRequest of = PageRequest.of(1, 10);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(of);
        SearchHits<User> search = elasticsearchRestTemplate.search(queryBuilder.build(), User.class);
        List<SearchHit<User>> list = search.getSearchHits();
        log.info("list 查询: {}", JSONUtil.toJsonStr(list));
    }


}
