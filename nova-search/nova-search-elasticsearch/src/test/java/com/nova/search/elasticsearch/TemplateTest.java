package com.nova.search.elasticsearch;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.nova.search.elasticsearch.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: wzh
 * @description: 模板方法测试类
 * @date: 2024/04/28 10:30
 */
@Slf4j
@SpringBootTest
public class TemplateTest {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    //新增单条
    @Test
    public void insertOne() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("张三_" + i);
            user.setPassword("password_" + i);
            user.setCreateTime(new Date());
            elasticsearchRestTemplate.save(user);
        }
    }

    //新增多条
    @Test
    public void insertList() {
        List<IndexQuery> indexQueryList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserName("张三_" + i);
            user.setPassword("password_" + i);
            user.setCreateTime(new Date());

            indexQueryList.add(new IndexQueryBuilder()
                    //id可以不写
                    .withId(String.valueOf(i))
                    .withObject(user)
                    .build());
        }
        elasticsearchRestTemplate.bulkIndex(indexQueryList, elasticsearchRestTemplate.getIndexCoordinatesFor(User.class));
    }


    //删除
    @Test
    public void delete() {
        String delete = elasticsearchRestTemplate.delete("5", User.class);
        Console.log("delete {} ", delete);
    }

    //删除多个
    @Test
    public void deleteList() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termsQuery("id", Arrays.asList("1", "2", "3", "4", "5")))
                .build();
        ByQueryResponse delete = elasticsearchRestTemplate.delete(query, User.class, elasticsearchRestTemplate.getIndexCoordinatesFor(User.class));
        Console.log("delete {} ", delete.getDeleted());
    }

    //修改（与Field写入字段不是一个，一个按照注解写入，一个按照属性名称写入）
    @Test
    public void update() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUserName("张三_" + 2);
        user.setPassword("password_" + 3);

        UpdateQuery builder = UpdateQuery
                .builder(String.valueOf(user.getId()))
                .withDocument(Document.parse(JSONUtil.toJsonStr(user)))
                .build();
        UpdateResponse update = elasticsearchRestTemplate.update(builder, elasticsearchRestTemplate.getIndexCoordinatesFor(User.class));
        System.out.println("1 = " + 1);
    }

    //基础查询
    @Test
    public void queryPage() {
        //分页 + 排序
        Pageable page = PageRequest.of(0, 3, Sort.Direction.ASC, "id");

        //排序2
        FieldSortBuilder sort = new FieldSortBuilder("id").order(SortOrder.ASC);

        //构造条件
        //精准匹配
        TermQueryBuilder id = QueryBuilders.termQuery("id", 1L);
        //过滤条件
        RangeQueryBuilder balance = QueryBuilders.rangeQuery("balance").gte(20000).lte(30000);

        //must-与、should-或、must_not-非
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("id", 1L))
                .must(QueryBuilders.matchQuery("username", "张三_1"));


        //构建查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                //分页
                .withPageable(page)
                //排序2
//                .withSorts(sort)
                .build();
        //查询
        SearchHits<User> search = elasticsearchRestTemplate.search(searchQuery, User.class);
        List<User> userList = processSearch(search);
        Console.log("jsonStr：{} ", JSONUtil.toJsonStr(userList));
    }

    //模糊查询
    @Test
    public void likeQuery() {
        //构造关键字，*类比sql中like %
        QueryBuilder key = QueryBuilders.wildcardQuery("username", "张三*");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(key);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();
        SearchHits<User> search = elasticsearchRestTemplate.search(searchQuery, User.class);
        List<User> userList = processSearch(search);
        Console.log("jsonStr：{} ", JSONUtil.toJsonStr(userList));
    }

    //高亮查询
    @Test
    public void highlight() {
        //根据一个值查询多个字段并高亮显示这里的查询是取并集，即多个字段只需要有一个字段满足即可
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("username", "张三*"))
                .should(QueryBuilders.matchQuery("password", "password*"));

        //构建高亮查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(new HighlightBuilder.Field("username"), new HighlightBuilder.Field("password"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>"))
                .build();

        //查询
        SearchHits<User> search = elasticsearchRestTemplate.search(searchQuery, User.class);
        List<SearchHit<User>> searchHits = search.getSearchHits();

        //高亮结果处理
        List<User> userList = new ArrayList<>();
        for (SearchHit<User> searchHit : searchHits) {
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setUserName(highlightFields.get("userName") == null ? searchHit.getContent().getUserName() : highlightFields.get("userName").get(0));
            searchHit.getContent().setPassword(highlightFields.get("password") == null ? searchHit.getContent().getPassword() : highlightFields.get("password").get(0));
            userList.add(searchHit.getContent());
        }
        Console.log("jsonStr：{} ", JSONUtil.toJsonStr(userList));
    }


    //或者用maps的convert进行转换
    private static <T> List<T> processSearch(SearchHits<T> search) {
        //设置一个最后需要返回的实体类集合
        List<T> list = new ArrayList<>();
        //得到查询返回的内容
        List<SearchHit<T>> searchHits = search.getSearchHits();
        //遍历返回的内容进行处理
        for (SearchHit<T> searchHit : searchHits) {
            list.add(searchHit.getContent());
        }
        return list;
    }
}
