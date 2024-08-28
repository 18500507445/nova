package com.nova.search.elasticsearch;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.nova.search.elasticsearch.entity.PageResult;
import com.nova.search.elasticsearch.repository.User;
import com.nova.search.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
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
public class EsTemplateTest {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public static final String INDEX_NAME = "test_create_index";

    //创建索引 + 索引是否存在
    @Test
    public void createTest() {
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(INDEX_NAME));
        if (!indexOps.exists()) {
            indexOps.create();
            Console.log("索引创建成功");
        } else {
            Console.log("索引已存在");
        }
    }

    //删除索引
    @Test
    public void deleteTest() {
        boolean delete = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(INDEX_NAME)).delete();
        Console.log("delete {} ", delete);
    }

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
        TimeInterval timer = DateUtil.timer();
        List<IndexQuery> indexQueryList = new ArrayList<>();
        for (int i = 1; i <= 50000; i++) {
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
        Console.error("耗时 = {} ms", timer.interval());
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
        System.err.println("update = " + update);
    }

    //fixme 创建索引并且设置最大返回条数，报错：提示索引已经创建（删除再执行也不行）
    @Test
    public void maxResultWindow() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of("user"));
        Map<String, Object> settings = new HashMap<>();
        settings.put("index.max_result_window", Integer.MAX_VALUE);
        indexOperations.create(settings);
    }


    //基础查询
    @Test
    public void queryPage() {
        TimeInterval timer = DateUtil.timer();
        //⚠️注意：ES页码从0开始
        int pageIndex = 1;

        // ----------------------------分页条件----------------------------

        //分页 + 排序
        Pageable page = PageRequest.of(pageIndex - 1, 200, Sort.Direction.ASC, "id");
        //排序-2
        FieldSortBuilder sort = new FieldSortBuilder("id").order(SortOrder.ASC);
        //排序-3
        FieldSortBuilder sort2 = SortBuilders.fieldSort("id").order(SortOrder.DESC);

        // ----------------------------构造条件----------------------------

        //精准查询-不分词。解释：id = 1的文档
        QueryBuilder equal = QueryBuilders.termQuery("id", 1L);
        QueryBuilder in = QueryBuilders.termsQuery("id", Arrays.asList(1L, 2L, 3L));

        //模糊查询-单字段，分词
        QueryBuilder match = QueryBuilders.matchQuery("address", "天台");

        //模糊查询-多字段，分词
        QueryBuilder multi = QueryBuilders.multiMatchQuery("常", "username", "address");
        //模糊查询-多字段，分词，不带field就是查询所有
        QueryBuilder string = QueryBuilders.queryStringQuery("常").field("username").field("address");

        //模糊查询-左右模糊查询，其中fuzziness的参数作用是在查询时，es动态的将查询关键词前后增加或者删除一个词，然后进行匹配
        QueryBuilder fuzzy = QueryBuilders.fuzzyQuery("idCard", "530").fuzziness(Fuzziness.ONE);
        //模糊查询-前缀
        QueryBuilder prefix = QueryBuilders.prefixQuery("idCard", "530");
        //模糊查询-通配符，不分词，* 或 ？类比sql like%，不建议做为前缀，效率可能有影响
        QueryBuilder like = QueryBuilders.wildcardQuery("idCard", "530*");

        //范围查询-闭区间
        QueryBuilder close = QueryBuilders.rangeQuery("id").from(3).to(5);
        //范围查询-开区间
        QueryBuilder open = QueryBuilders.rangeQuery("id").from(3).to(5).includeLower(false).includeUpper(false);
        //范围查询-大于等于、小于等于
        QueryBuilder gtAndLt = QueryBuilders.rangeQuery("id").gte(3).lte(5);

        /*
          重点：组合查询-多个关键字分词查询（type = FieldType.Text）。must(与)、should(或)、mustNot(非)。
          @description: 举例：查找address带有天台和广场，不带街道的数据
         */
        BoolQueryBuilder multiple = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("address", "天台"))
                .must(QueryBuilders.termQuery("address", "广场"))
                .mustNot(QueryBuilders.termQuery("address", "街道"));


        //方式1，构建查询
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(string)
                //分页
                .withPageable(page)
                //返回所有命中条数
                .withTrackTotalHits(true)
                //排序2
//                .withSorts(sort)
                .build();


        //方式2，构建条件 + 查询
        Criteria criteria = new Criteria();
        criteria.and(Criteria.where("id").is(1L));
        criteria.and(Criteria.where("id").between(3, 5));
        criteria.and(Criteria.where("id").in(3, 5));
        Query query = new CriteriaQuery(criteria);

        //执行查询
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(searchQuery, User.class);
        PageResult<User> pageResult = EsUtils.page(searchHits, page);
        Console.error("pageResult：{} ", JSONUtil.toJsonStr(pageResult));
        Console.error("耗时：{} ", timer.interval());
    }


    //高亮查询
    @Test
    public void highlight() {
        //根据一个值查询多个字段并高亮显示这里的查询是取并集，即多个字段只需要有一个字段满足即可
        BoolQueryBuilder match = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("username", "马绍*"))
                .should(QueryBuilders.matchQuery("password", "5c1urru49p8d*"));

        //构建高亮查询
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(match)
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
        Console.error("jsonStr：{} ", JSONUtil.toJsonStr(userList));
    }

    //分组查询
    @Test
    public void groupBy() {
        String key = "group";
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        query.withQuery(boolQueryBuilder);

        // 添加一个新的聚合，聚合类型为terms，聚合名称为count，聚合字段为sex
        query.withAggregations(AggregationBuilders.terms(key).field("sex"));
        // 不需要获取source结果集，在aggregation里可以获取结果
        query.withSourceFilter(new FetchSourceFilterBuilder().build());

        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query.build(), User.class);

        Aggregation aggregation = EsUtils.getAgg(searchHits, key);
        Console.error("aggregation：{}", JSONUtil.toJsonStr(aggregation));

        List<Object> keys = EsUtils.getAggKeys(searchHits, key);
        Console.error("keys：{}", JSONUtil.toJsonStr(keys));

        List<? extends Terms.Bucket> buckets = EsUtils.getBuckets(searchHits, key);
        Console.error("buckets：{}", JSONUtil.toJsonStr(buckets));

        List<Map<String, Object>> listMap = EsUtils.getBucketsMap(searchHits, key, Terms.class);
        Console.error("listMap：{}", JSONUtil.toJsonStr(listMap));
    }

    //嵌套聚合，性别分组后，求平均年龄
    @Test
    public void subAgg() {
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        // 不查询任何结果
        query.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));

        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为count，聚合字段为sex
        query.withAggregations(
                AggregationBuilders.terms("group").field("sex")
                        // 在性别聚合桶内进行嵌套聚合，求平均值
                        .subAggregation(AggregationBuilders.avg("avg").field("age"))
                        // 聚合之后返回条数
                        .size(Integer.MAX_VALUE)
        );

        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query.build(), User.class);

        List<? extends Terms.Bucket> buckets = EsUtils.getBuckets(searchHits, "group");
        Console.error("buckets：{}", JSONUtil.toJsonStr(buckets));
    }


    /**
     * 翻译一个sql
     * 举例，参考user-json数据{@link src/main/resources/user.json}
     * select * from user where id = 204980 and idCard = 152501198306232651 and (address like '%123%' or address like '%345%')
     */
    @Test
    public void sql() {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("id", 598720));
        boolQuery.must(QueryBuilders.termQuery("idCard", "653100198706089920"));

        //如果字段是分词TEXT，就不能用wildcardQuery
        BoolQueryBuilder addressQuery = QueryBuilders.boolQuery();
        addressQuery.should(QueryBuilders.wildcardQuery("password", "*6d4*"));
        addressQuery.should(QueryBuilders.wildcardQuery("password", "*6d4*"));
        boolQuery.must(addressQuery);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(searchQuery, User.class);
        List<User> list = EsUtils.list(searchHits);
        Console.error("list：{} ", JSONUtil.toJsonStr(list));
    }

}
