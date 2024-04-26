package com.nova.search.elasticsearch;

import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.nova.search.elasticsearch.dao.UserRepository;
import com.nova.search.elasticsearch.entity.User;
import com.nova.search.elasticsearch.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void insertTest() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("张三_" + i);
            user.setPassword("password_" + i);
            userService.save(user);
        }
    }

    @Test
    public void findByIdTest() {
        Optional<User> entity = userService.findById("1");
        entity.ifPresent(user -> log.info("查询成功：{}", JSONUtil.toJsonStr(user)));
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
        user.setId(1L);
        userService.delete(user);
    }

    /**
     * 其它api
     */
    @Test
    public void otherFunctions() {
        List<String> idList = Arrays.asList("1", "2");
        //批量查询
        userRepository.findAllById(idList);
        //批量删除
//        userRepository.deleteAllById(idList);
        //批量保存
        List<User> userList = new ArrayList<>();
        userRepository.saveAll(userList);
        //查询所有
        userRepository.findAll();

        //分页查询
        Pageable page = PageRequest.of(0, 3, Sort.Direction.ASC, "id");
        Page<User> pageList = userRepository.findAll(page);
        System.err.println("pageList = " + pageList);
    }

    /**
     * 分页查询
     */
    @Test
    public void queryPage() {
        Pageable page = PageRequest.of(0, 3, Sort.Direction.ASC, "id");

        //根据一个值查询多个字段并高亮显示这里的查询是取并集，即多个字段只需要有一个字段满足即可
        //需要查询的字段
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("username", "张三"))
                .should(QueryBuilders.matchQuery("password", "password"));
        //构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(
                        new HighlightBuilder.Field("username")
                        , new HighlightBuilder.Field("password"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                //分页
//                .withPageable(page)
                .build();
        //查询
        SearchHits<User> search = elasticsearchRestTemplate.search(searchQuery, User.class);
        //得到查询返回的内容
        List<SearchHit<User>> searchHits = search.getSearchHits();
        //设置一个最后需要返回的实体类集合
        List<User> userList = new ArrayList<>();
        //遍历返回的内容进行处理
        for (SearchHit<User> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setUsername(highlightFields.get("username") == null ? searchHit.getContent().getUsername() : highlightFields.get("username").get(0));
            searchHit.getContent().setPassword(highlightFields.get("password") == null ? searchHit.getContent().getPassword() : highlightFields.get("password").get(0));
            //放到实体类中
            userList.add(searchHit.getContent());
        }
        String jsonStr = JSONUtil.toJsonStr(userList);
        System.err.println("jsonStr = " + jsonStr);
    }

    /**
     * 模糊查询
     */
    @Test
    public void likeQuery() {
        String userName = "张三";
        String password = "password";

        //查询对象
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        //模糊搜索对象
        BoolQueryBuilder keyBuilder = new BoolQueryBuilder();
        keyBuilder.should(QueryBuilders.wildcardQuery("username", "*" + userName + "*"));
        keyBuilder.should(QueryBuilders.wildcardQuery("password", "*" + password + "*"));
        queryBuilder.must(keyBuilder);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        SearchHits<User> search = elasticsearchRestTemplate.search(searchQuery, User.class);

        List<User> userList = new ArrayList<>();
        //得到查询返回的内容
        List<SearchHit<User>> searchHits = search.getSearchHits();
        for (SearchHit<User> searchHit : searchHits) {
            //放到实体类中
            userList.add(searchHit.getContent());
        }
        String jsonStr = JSONUtil.toJsonStr(userList);
        System.err.println("jsonStr = " + jsonStr);
    }

    @Autowired
    @Deprecated
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

        //按照id从下标几开始查询
        searchSourceBuilder.from(1);
        searchSourceBuilder.size(3);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (org.elasticsearch.search.SearchHit hit : searchResponse.getInternalResponse().hits().getHits()) {
            log.info("client 查询: {}", JSONUtil.toJsonStr(hit.getSourceAsMap()));
        }
    }

    @Test
    public void queryByRestClient() throws IOException {
        // 创建低级客户端
        RestClient restClient = RestClient.builder(new HttpHost("225297c275c2.c.methodot.com", 9200)).build();
        // 使用Jackson映射器创建传输层
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // 创建API客户端
        ElasticsearchClient client = new ElasticsearchClient(transport);

        String indexName = "user";

        //删除
        DeleteResponse delete = client.delete(e -> e.index(indexName).id("1"));
        System.err.println(delete.result());

        //查询
        GetResponse<User> query = client.get(e -> e.index(indexName).id("1"), User.class);
        System.err.println("user = " + JSONUtil.toJsonStr(query.source()));

        //修改
        Map<String, Object> map = new HashMap<>();
        map.put("username", "username");
        UpdateResponse<User> update = client.update(e -> e.index(indexName).id("1").doc(map), User.class);
        System.err.println(update.result());

        //创建
        User user = new User();
        CreateResponse create = client.create(e -> e.index(indexName).id("1").document(user));
        System.err.println(create.result());

        //批量添加
        List<BulkOperation> list = new ArrayList<>();
        user.setPassword("password");
        user.setUsername("username");
        list.add(new BulkOperation.Builder()
                //删除替换delete
                .create(d -> d.document(user).id("1").index(indexName))
                .build());
        BulkResponse bulkResponse = client.bulk(e -> e.index(indexName).operations(list));
        System.err.println(bulkResponse.toString());

        // 关闭ES客户端
        transport.close();
        restClient.close();
    }


}
