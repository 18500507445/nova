package com.nova.search.elasticsearch;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.nova.search.elasticsearch.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
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
import java.util.stream.Collectors;

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
        long totalHits = search.getTotalHits();
        List<User> userList = processSearch(search);
        Console.log("totalCount {} ", totalHits);
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

    //聚合查询
    @Test
    public void demoA() {
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.matchQuery("password", "password_1"));
        query.withQuery(boolQueryBuilder);
        // 作为聚合的字段不能是text类型。所以，author的mapping要有keyword，且通过password聚合。
        query.withAggregations(AggregationBuilders.terms("count").field("password"));
        // 不需要获取source结果集，在aggregation里可以获取结果
        query.withSourceFilter(new FetchSourceFilterBuilder().build());

        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query.build(), User.class);
        Map<String, Aggregation> aggregationMap = ((Aggregations) Objects.requireNonNull(searchHits.getAggregations()).aggregations()).asMap();

        Console.log("aggregationMap：{} ", JSONUtil.toJsonStr(aggregationMap));
        Aggregation count = aggregationMap.get("count");
        System.out.println("count = " + JSONUtil.toJsonStr(count));
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

    /**
     * @param key       : es里索引的域(字段名)
     * @param classType : 返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @param values    : 一域多值, 查询的值
     * @description: 词条查询(不分前端传过来的数据)
     * <p>
     * term查询，查询text类型字段时，只有其中的单词相匹配都会查到，text字段会对数据进行分词
     * term查询，查询keyword类型字段时，只有完全匹配才会查到，keyword字段不会对数据进行分词
     * term query会去倒排索引中寻找确切的term，它并不知道分词器的存在。这种查询适合keyword 、numeric、date
     */
    public <T> List<T> termQuery(String key, Class<T> classType, String... values) {
        //查询条件(词条查询：对应ES query里的term)
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(key, values);
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery searchQuery = new NativeSearchQuery(termsQueryBuilder);
        //查询,获取查询结果
        SearchHits<T> search = elasticsearchRestTemplate.search(searchQuery, classType);
        //获取值返回
        return search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Test
    public void testTermQuery() {
        List<User> userList = termQuery("password", User.class, "password_1", "password_2");
        Console.log("jsonStr：{} ", JSONUtil.toJsonStr(userList));
    }

    /**
     * 例如入参分词: 山东省济南市  ik_smart粗粒度:[山东省,济南市] ik_max_word细粒度:[山东省,山东,省,济南市,济南,南市]
     *
     * @param operator  : Operator.OR(并集) [默认] 只要分的词有一个和索引字段上对应上则就返回，Operator.AND(交集)   分的词全部满足的数据返回
     * @param analyzer  : 选择分词器[ik_smart粗粒度,ik_max_word细粒度] 默认:ik_max_word细粒度
     * @param key       :  es里索引的域(字段名)
     * @param classType :  返回的list里的对象并且通过对象里面@Document注解indexName属性获取查询哪个索引
     * @param text      :  查询的值
     * @description ：matchQuery:词条分词查询(会对查询条件进行分词)
     */
    public <T> List<T> matchQuery(Operator operator, String analyzer, String key, Class<T> classType, String text) {
        //查询条件(词条查询：对应ES query里的match)
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(key, text).analyzer(analyzer).operator(operator);
        //创建查询条件构建器SearchSourceBuilder(对应ES外面的大括号)
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(matchQueryBuilder);
        //查询,获取查询结果
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, classType);
        //获取值返回
        return search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Test
    public void testMatchQuery() {
        List<User> userList = matchQuery(Operator.AND, "ik_smart", "password", User.class, "password_1");
        Console.log("jsonStr：{} ", JSONUtil.toJsonStr(userList));
    }
}
