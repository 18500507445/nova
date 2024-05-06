## es学习

### [官方文档](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html)

### 名词解释
1.索引（Index）：索引是Elasticsearch中存储、搜索和分析数据的地方，类似于关系型数据库中的表。一个索引可以包含多个文档，并且可以分布在一个或多个分片上。
2.类型（Type）：在Elasticsearch6.0版本之前，一个索引可以包含多个类型。类型允许你对索引中的文档进行更细粒度的分类。然而，从Elasticsearch6.0开始，类型被废弃，一个索引只能有一个默认类型"_doc"。
3.文档（Document）：文档是Elasticsearch中的最小数据单元。它是以JSON格式表示的一条记录或一份数据。每个文档都属于一个索引，并具有一个唯一的标识（_id）。
4.分片（Shard）：索引可以被分为多个分片。每个分片是一个独立的索引单元，它包含了完整的索引数据。分片允许数据分布在多个节点上，以提高性能和可扩展性。
5.副本（Replica）：副本是分片的复制。每个分片可以有零个或多个副本。副本用于提供冗余和故障恢复。副本还可以提高搜索性能，因为搜索请求可以并行在主分片和副本上执行。
6.节点（Node）：节点是Elasticsearch集群中的一个服务器，它存储数据并参与集群的索引和搜索操作。一个节点可以是主节点（负责协调集群操作）或数据节点（存储和处理数据）。
7.集群（Cluster）：一个集群由一个或多个节点组成，它们共同协作来存储数据和执行操作。集群提供了高可用性、扩展性和容错能力。
8.查询（Query）：查询是用于从Elasticsearch中检索数据的操作。Elasticsearch提供了丰富的查询语言和API，包括全文搜索、精确匹配、范围查询、聚合等功能。
9.聚合（Aggregation）：聚合是在Elasticsearch中进行数据分析的一种功能。它允许你对数据进行分组、计算统计信息、生成报告等，以获取有关数据的洞察力。

### API说明
* NativeSearchQuery ：是Spring data中的查询条件
* NativeSearchQueryBuilder ：建造一个NativeSearchQuery查询对象
* QueryBuilders ：设置查询条件，是ES中的类，查询条件构构造器
* SortBuilders ：设置排序条件
* HighlightBuilder ：设置高亮显示

### 桶的说明
* Terms Bucket（术语桶）：根据字段值进行分组，类似于SQL中的GROUP BY。可以统计每个术语的文档数量或其他指标。
* Range Bucket（范围桶）：根据字段值的范围进行分组，可以定义多个范围并统计每个范围内的文档数量或其他指标。
* Date Histogram Bucket（日期直方图桶）：根据日期字段的时间间隔进行分组，例如按年、月、周等。可以统计每个时间间隔内的文档数量或其他指标。
* Histogram Bucket（直方图桶）：根据数值字段的间隔进行分组，例如按价格区间、年龄段等。可以统计每个间隔内的文档数量或其他指标。
* Geo Distance Bucket（地理距离桶）：根据地理位置字段的距离进行分组，例如按半径范围内的位置。可以统计每个距离范围内的文档数量或其他指标。
* Filter Bucket（过滤桶）：根据给定的过滤条件对文档进行过滤并创建一个桶。可以在桶内统计过滤后的文档数量或其他指标。
* Nested Bucket（嵌套桶）：用于在嵌套文档结构中进行分组，类似于在嵌套对象上进行递归操作。

### 常见问题
* [ES查询超过限制，并且NativeSearchQuery设置max数](https://www.cnblogs.com/datangguanjunhou/p/16482242.html)

### 工具类如何使用
1.实体类打上@EsRepository注解
2.创建索引、判断索引、基础的增删改查、查询结果的聚合拆解都在EsUtils里面
3.EsUtils代码如下
~~~java
package com.nova.search.elasticsearch.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.nova.search.elasticsearch.annotation.EsRepository;
import com.nova.search.elasticsearch.entity.PageResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoGrid;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description: ES工具类
 * @date: 2024/04/29 09:03
 */
@Slf4j(topic = "avic-parent-starter ==> EsUtils")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EsUtils {

    //-------------------------- ElasticsearchRestTemplate索引方法 --------------------------

    /**
     * 创建索引
     *
     * @param clazz 类.class
     * @param <T>   泛型
     * @description: 需要类打上@Document(indexName = "xx")注解才可以使用
     */
    public static <T> boolean indexCreate(Class<T> clazz) {
        IndexOperations indexOps = indexCreate(null, clazz);
        return indexCreate(indexOps);
    }

    public static boolean indexCreate(String name) {
        IndexOperations indexOps = indexCreate(name, null);
        return indexCreate(indexOps);
    }

    /**
     * 判断索引是否存在
     *
     * @param clazz 类
     * @param <T>   泛型
     * @description: 需要类打上@Document(indexName = "xx")注解才可以使用
     */
    public static <T> boolean indexExists(Class<T> clazz) {
        IndexOperations indexOps = indexCreate(null, clazz);
        return indexOps.exists();
    }

    public static boolean indexExists(String name) {
        IndexOperations indexOps = indexCreate(name, null);
        return indexOps.exists();
    }

    /**
     * 删除索引
     *
     * @param clazz 类
     * @param <T>   泛型
     * @description: 需要类打上@Document(indexName = "xx")注解才可以使用
     */
    public static <T> boolean indexDelete(Class<T> clazz) {
        IndexOperations indexOps = indexCreate(null, clazz);
        return indexOps.delete();
    }

    public static boolean indexDelete(String name) {
        IndexOperations indexOps = indexCreate(name, null);
        return indexOps.delete();
    }

    //------------- ElasticsearchRepository方法封装，类上学要@EsRepository注解，标注仓库接口 -------------

    /**
     * 新增数据
     *
     * @param bean 数据对象
     */
    @SuppressWarnings("unchecked")
    public static <T> void save(T bean) {
        EsRepository esRepository = bean.getClass().getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(bean);
        }
        CrudRepository<T, ?> crudRepository = (CrudRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        crudRepository.save(bean);
    }

    /**
     * 批量新增
     *
     * @param list list
     * @param <T>  t
     */
    @SuppressWarnings("unchecked")
    public static <T> void saveAll(List<T> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        EsRepository esRepository = list.get(0).getClass().getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(list.get(0));
        }
        CrudRepository<T, ?> crudRepository = (CrudRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        crudRepository.saveAll(list);
    }

    /**
     * 根据id查询单个对象
     *
     * @param id        id
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T, ID> T findById(ID id, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ID> crudRepository = (CrudRepository<T, ID>) SpringUtil.getBean(esRepository.value());
        Optional<T> entity = crudRepository.findById(id);
        return entity.orElse(null);
    }

    /**
     * 根据idList查询list
     *
     * @param ids       ids
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T, ID> List<T> findAllById(Iterable<ID> ids, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ID> crudRepository = (CrudRepository<T, ID>) SpringUtil.getBean(esRepository.value());
        return (List<T>) crudRepository.findAllById(ids);
    }

    /**
     * 查找所有
     *
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> findAll(Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ?> crudRepository = (CrudRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        Iterable<T> all = crudRepository.findAll();
        List<T> list = new ArrayList<>();
        all.forEach(list::add);
        return list;
    }

    /**
     * 查找所有，支持排序
     *
     * @param sort      {@link Sort}
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> findAll(Sort sort, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        PagingAndSortingRepository<T, ?> crudRepository = (PagingAndSortingRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        Iterable<T> all = crudRepository.findAll(sort);
        List<T> list = new ArrayList<>();
        all.forEach(list::add);
        return list;
    }

    /**
     * 查找所有，支持分页
     *
     * @param pageable  {@link Pageable}
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> findAll(Pageable pageable, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        PagingAndSortingRepository<T, ?> crudRepository = (PagingAndSortingRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        return crudRepository.findAll(pageable);
    }

    /**
     * 模糊查询，支持分页
     *
     * @param bean     对象
     * @param fields   字段数组
     * @param pageable {@link Pageable}
     */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> findAll(T bean, @Nullable String[] fields, Pageable pageable) {
        EsRepository esRepository = bean.getClass().getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(bean);
        }
        ElasticsearchRepository<T, ?> crudRepository = (ElasticsearchRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        return crudRepository.searchSimilar(bean, fields, pageable);
    }

    /**
     * 判断是否存在
     *
     * @param id        id
     * @param beanClass beanClass
     */
    @SuppressWarnings("unchecked")
    public static <T, ID> boolean existsById(ID id, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ID> crudRepository = (CrudRepository<T, ID>) SpringUtil.getBean(esRepository.value());
        return crudRepository.existsById(id);
    }

    /**
     * 统计个数
     *
     * @param beanClass beanClass
     */
    @SuppressWarnings("unchecked")
    public static <T> long count(Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ?> crudRepository = (CrudRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        return crudRepository.count();
    }

    /**
     * 删除单个（根据bean.id删除）
     *
     * @param bean bean
     */
    @SuppressWarnings("unchecked")
    public static <T> void delete(T bean) {
        EsRepository esRepository = bean.getClass().getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(bean);
        }
        CrudRepository<T, ?> crudRepository = (CrudRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        crudRepository.delete(bean);
    }

    /**
     * 删除一个，根据id
     *
     * @param id        id
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T, ID> void deleteById(ID id, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ID> crudRepository = (CrudRepository<T, ID>) SpringUtil.getBean(esRepository.value());
        crudRepository.deleteById(id);
    }

    /**
     * 删除多个，根据ids
     *
     * @param ids       ids
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T, ID> void deleteAllById(Iterable<ID> ids, Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ID> crudRepository = (CrudRepository<T, ID>) SpringUtil.getBean(esRepository.value());
        crudRepository.deleteAllById(ids);
    }

    /**
     * 删除所有
     *
     * @param beanClass class
     */
    @SuppressWarnings("unchecked")
    public static <T> void deleteAll(Class<T> beanClass) {
        EsRepository esRepository = beanClass.getAnnotation(EsRepository.class);
        if (null == esRepository) {
            printError(beanClass);
        }
        CrudRepository<T, ?> crudRepository = (CrudRepository<T, ?>) SpringUtil.getBean(esRepository.value());
        crudRepository.deleteAll();
    }

    //-------------------------- ES基础方法，SearchHits转换、分页、获取桶的属性和key、字段类型等 --------------------------

    /**
     * SearchHits转换List
     *
     * @param searchHits {@link SearchHits}
     */
    public static <T> List<T> list(SearchHits<T> searchHits) {
        if (searchHits != null && searchHits.hasSearchHits()) {
            List<T> list = new ArrayList<>();
            searchHits.forEach(searchHit -> list.add(searchHit.getContent()));
            return list;
        }
        return null;
    }

    /**
     * 对ES返回结果进行包装
     *
     * @param searchHits {@link SearchHits}
     * @param pageable   {@link Pageable}
     * @param <T>        t
     */
    public static <T> PageResult<T> page(SearchHits<T> searchHits, Pageable pageable) {
        if (searchHits != null && searchHits.hasSearchHits()) {
            long totalCount = searchHits.getTotalHits();
            if (totalCount <= 0) {
                PageResult.empty();
            }
            List<T> content = list(searchHits);
            Page<T> page = new PageImpl<>(content, pageable, totalCount);
            //⚠️注意：ES页码从0开始，需 +1
            return PageResult.page(content, page.getNumber() + 1, page.getSize(), totalCount);
        }
        return null;
    }

    /**
     * @param clazz class
     * @param key   字段名
     * @description: 获取字段类型
     */
    @SneakyThrows
    public static <T> @Nullable FieldType getFieldType(Class<T> clazz, String key) {
        FieldType type = null;
        if (!key.contains(".")) {
            Field f = clazz.getDeclaredField(key);
            if (f.isAnnotationPresent(org.springframework.data.elasticsearch.annotations.Field.class)) {
                type = f.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class).type();
            } else if (f.isAnnotationPresent(MultiField.class)) {
                type = f.getAnnotation(MultiField.class).mainField().type();
            }
        } else {
            //如果字段Field type定义的是Keyword，走matchQuery效果也是term精确查询
            type = FieldType.Text;
        }
        return type;
    }

    /**
     * @param aggregations {@link Aggregations}
     * @param key          key
     * @return List List<keyAsString>
     * @description: 根据key获取聚合中的key列表，类型自己转换
     */
    public static List<Object> getAggKeys(Aggregations aggregations, String key) {
        if (null == aggregations) {
            return null;
        }
        Terms terms = aggregations.get(key);
        return terms.getBuckets().stream()
                .map(MultiBucketsAggregation.Bucket::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @return List List<keyAsString>
     * @description: 根据key获取聚合中的key列表，类型自己转换
     */
    public static <T> List<Object> getAggKeys(SearchHits<T> searchHits, String key) {
        if (null == searchHits) {
            return null;
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return null;
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        return getAggKeys(aggregations, key);
    }

    /**
     * @param aggregations {@link Aggregations}
     * @param key          key
     * @description: 根据key获取聚合
     */
    public static Aggregation getAgg(Aggregations aggregations, String key) {
        if (null == aggregations) {
            return null;
        }
        return aggregations.asMap().get(key);
    }

    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @description: 根据key获取聚合
     */
    public static <T> Aggregation getAgg(SearchHits<T> searchHits, String key) {
        if (null == searchHits) {
            return null;
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return null;
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        return getAgg(aggregations, key);
    }

    /**
     * @param aggregations {@link Aggregations}
     * @param key          key
     * @description: 根据key获取所有的桶，仅支持Terms类型
     */
    public static List<? extends Terms.Bucket> getBuckets(Aggregations aggregations, String key) {
        if (null == aggregations) {
            return null;
        }
        Terms terms = aggregations.get(key);
        return terms.getBuckets();
    }


    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @description: 根据key获取所有的桶，仅支持Terms类型
     */
    public static <T> List<? extends Terms.Bucket> getBuckets(SearchHits<T> searchHits, String key) {
        if (null == searchHits) {
            return null;
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return null;
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        return getBuckets(aggregations, key);
    }

    /**
     * @param aggregations {@link Aggregations}
     * @param key          key
     * @return : List<Bucket> ==> Map<key, docCount>
     * @description: 根据key获取所有桶，然后转List<Map>，仅支持Terms类型
     */
    public static List<Map<String, Object>> getBucketsMap(Aggregations aggregations, String key) {
        if (null == aggregations) {
            return null;
        }
        Terms terms = aggregations.get(key);
        return JSON.parseObject(JSONObject.toJSONString(terms.getBuckets()), new TypeReference<List<Map<String, Object>>>() {
        });
    }

    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @return : List<Bucket> ==> List<Map<String, Object>>
     * @description: 根据key获取所有桶，然后转List<Map>，仅支持Terms类型
     */
    public static <T> List<Map<String, Object>> getBucketsMap(SearchHits<T> searchHits, String key) {
        if (null == searchHits) {
            return null;
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return null;
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        return getBucketsMap(aggregations, key);
    }

    /**
     * @param aggregations {@link Aggregations}
     * @param key          key
     * @param bucketType   支持{@link Terms,Range,Histogram,Filters,GeoGrid}
     * @return : List<Bucket> ==> Map<key, docCount>
     * @description: 根据key获取所有桶，然后转List<Map>
     */
    public static <T> List<Map<String, Object>> getBucketsMap(Aggregations aggregations, String key, Class<T> bucketType) {
        if (null == aggregations || StrUtil.isBlank(key) || null == bucketType) {
            return null;
        }
        if (Terms.class.equals(bucketType)) {
            Terms terms = aggregations.get(key);
            return JSON.parseObject(JSONObject.toJSONString(terms.getBuckets()), new TypeReference<List<Map<String, Object>>>() {
            });
        }
        if (Range.class.equals(bucketType)) {
            Range range = aggregations.get(key);
            return JSON.parseObject(JSONObject.toJSONString(range.getBuckets()), new TypeReference<List<Map<String, Object>>>() {
            });
        }
        if (Histogram.class.equals(bucketType)) {
            Histogram histogram = aggregations.get(key);
            return JSON.parseObject(JSONObject.toJSONString(histogram.getBuckets()), new TypeReference<List<Map<String, Object>>>() {
            });
        }
        if (Filters.class.equals(bucketType)) {
            Filters filters = aggregations.get(key);
            return JSON.parseObject(JSONObject.toJSONString(filters.getBuckets()), new TypeReference<List<Map<String, Object>>>() {
            });
        }
        if (GeoGrid.class.equals(bucketType)) {
            GeoGrid geoGrid = aggregations.get(key);
            return JSON.parseObject(JSONObject.toJSONString(geoGrid.getBuckets()), new TypeReference<List<Map<String, Object>>>() {
            });
        }
        return null;
    }

    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @return : List<Bucket> ==> List<Map<String, Object>>
     * @description: 根据key获取所有桶，然后转List<Map>
     */
    public static <T> List<Map<String, Object>> getBucketsMap(SearchHits<T> searchHits, String key, Class<? extends MultiBucketsAggregation> bucketType) {
        if (null == searchHits) {
            return null;
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return null;
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        return getBucketsMap(aggregations, key, bucketType);
    }


    /*----------------------------------------- private 私有方法 --------------------------------------------*/


    //获取es模板方法
    private static ElasticsearchRestTemplate getTemplate() {
        return SpringUtil.getBean(ElasticsearchRestTemplate.class);
    }

    //运行异常，提示语句
    private static <T> void printError(T t) {
        String simpleName = t.getClass().getSimpleName();
        throw new RuntimeException(simpleName + "类打上@EsRepository(" + simpleName + "Repository.class)注解");
    }

    //创建索引，提示
    private static boolean indexCreate(IndexOperations indexOps) {
        String indexName = indexOps.getIndexCoordinates().getIndexName();
        if (!indexOps.exists()) {
            indexOps.create();
            log.info("索引 【{}】 创建成功", indexName);
            return true;
        } else {
            log.info("索引 【{}】 创建失败", indexName);
            return false;
        }
    }

    //创建索引的2种方式
    private static <T> IndexOperations indexCreate(String name, Class<T> clazz) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getTemplate();
        IndexOperations indexOps = null;
        try {
            if (null != clazz) {
                indexOps = elasticsearchRestTemplate.indexOps(clazz);
            } else {
                indexOps = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(name));
            }
        } catch (Exception e) {
            log.error("indexCreate异常", e);
        }
        return indexOps;
    }

}
~~~


### 基础查询代码
~~~java
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

        //精准查询-不分词查询。解释：id = 1的文档
        QueryBuilder equal = QueryBuilders.termQuery("id", 1L);
        QueryBuilder in = QueryBuilders.termsQuery("id", Arrays.asList(1L, 2L, 3L));

        //精准查询-分词查询
        QueryBuilder match = QueryBuilders.matchQuery("address", "天台");
        //精准查询-分词查询-匹配多个
        QueryBuilder multi = QueryBuilders.multiMatchQuery("安", "username", "address");

        //精准查询-匹配field查询，支持分词，不带field就是查询所有
        QueryBuilder string = QueryBuilders.queryStringQuery("蓝").field("username").field("address");

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

        //组合查询-多个关键字分词查询（type = FieldType.Text）。must(与)、should(或)、mustNot(非)。举例：查找address带有天台和广场，不带有街道的数据
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("address", "天台"))
                .must(QueryBuilders.termQuery("address", "广场"))
                .mustNot(QueryBuilders.termQuery("address", "街道"));


        //方式1，构建查询
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                //分页
                .withPageable(page)
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
        );

        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query.build(), User.class);

        List<? extends Terms.Bucket> buckets = EsUtils.getBuckets(searchHits, "group");
        Console.error("buckets：{}", JSONUtil.toJsonStr(buckets));
    }
~~~