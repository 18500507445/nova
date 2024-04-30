package com.nova.search.elasticsearch.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.nova.search.elasticsearch.annotation.EsRepository;
import com.nova.search.elasticsearch.entity.PageResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
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
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /**
     * 对ES返回结果进行包装
     *
     * @param searchHits {@link SearchHits}
     * @param pageable   {@link Pageable}
     * @param <T>        t
     */
    public static <T> PageResult<T> page(SearchHits<T> searchHits, Pageable pageable) {
        long totalCount = searchHits.getTotalHits();
        if (totalCount <= 0) {
            PageResult.empty();
        }
        List<T> content = list(searchHits);
        Page<T> page = new PageImpl<>(content, pageable, totalCount);
        //⚠️注意：ES页码从0开始，需 +1
        return PageResult.page(content, page.getNumber() + 1, page.getSize(), totalCount);
    }

    /**
     * @param clazz class
     * @param key   字段名
     * @description: 获取字段类型
     */
    public static <T> @Nullable FieldType getFieldType(Class<T> clazz, String key) throws NoSuchFieldException {
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
            return new ArrayList<>();
        }
        Terms brandAgg = aggregations.get(key);
        return brandAgg.getBuckets().stream()
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
            return new ArrayList<>();
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return new ArrayList<>();
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        Terms terms = aggregations.get(key);
        return terms.getBuckets().stream()
                .map(MultiBucketsAggregation.Bucket::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @param aggregations {@link Aggregations}
     * @param key          key
     * @description: 根据key获取聚合
     */
    public static <T> Aggregation getAgg(Aggregations aggregations, String key) {
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


        Map<String, Aggregation> aggregationMap = ((Aggregations) aggregationsContainer.aggregations()).asMap();
        return aggregationMap.get(key);
    }

    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @description: 根据key获取所有的桶
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
        Terms terms = aggregations.get(key);
        return terms.getBuckets();
    }

    /**
     * @param searchHits {@link SearchHits}
     * @param key        key
     * @return : List<Bucket> ==> Map<key, docCount>
     * @description: 根据key获取桶中的key和docCount数量，建议成度：一般
     */
    public static <T> Map<Object, Long> getBucketsMap(SearchHits<T> searchHits, String key) {
        if (null == searchHits) {
            return null;
        }
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
        if (null == aggregationsContainer) {
            return null;
        }
        Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
        Terms terms = aggregations.get(key);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        Map<Object, Long> hashMap = new HashMap<>(16);
        for (Terms.Bucket bucket : buckets) {
            hashMap.put(bucket.getKey(), bucket.getDocCount());
        }
        return hashMap;
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
