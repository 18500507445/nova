package com.nova.search.elasticsearch.manage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.search.elasticsearch.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: wzh
 * @description: ES工具类
 * @date: 2024/04/28 15:25
 */
@Slf4j(topic = "ElasticsearchService")
@Component
@Deprecated
public class ElasticsearchService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 数据查询，返回List
     *
     * @return List<T>
     */
    public <T> List<T> queryList(T t) throws IllegalAccessException, NoSuchFieldException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Class<T> clazz = (Class<T>) t.getClass();
        Map<String, Object> queryMap = (LinkedHashMap) clazz.getField("fieldQueryMap").get(t);
        Map<String, Object> orderMap = (LinkedHashMap) clazz.getField("orderMap").get(t);
        List<String> highlightFields = (List<String>) clazz.getField("highlightFields").get(t);
        String preTags = StrUtil.isNotBlank((String) clazz.getField("preTags").get(t)) ? (String) clazz.getField("preTags").get(t) : "<em>";
        String postTags = StrUtil.isNotBlank((String) clazz.getField("postTags").get(t)) ? (String) clazz.getField("postTags").get(t) : "</em>";
        List<BaseEsEntity.MultiLayerRelation> multiLayerQueryList = (List<BaseEsEntity.MultiLayerRelation>) clazz.getField("multiLayerQueryList").get(t);

        String beanIdName = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(org.springframework.data.annotation.Id.class)) {
                beanIdName = f.getName();
                break;
            }
        }
        //构建组合查询(支持权重)
        getFieldQueryBuilder(boolQueryBuilder, clazz, queryMap);
        //处理多层bool查询
        getNestQueryBuilder(boolQueryBuilder, clazz, multiLayerQueryList);

        log.info("打印语句:{}", boolQueryBuilder);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder);
        //支持多字段排序查询
        getOrderBuilder(beanIdName, orderMap, nativeSearchQueryBuilder);
        //支持多字段高亮查询并可自定义高亮标签规则
        getHighLightBuilder(highlightFields, preTags, postTags, nativeSearchQueryBuilder);

        if (CollUtil.isEmpty(highlightFields)) {
            SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), clazz);
            //得到查询返回的内容
            List<SearchHit<T>> searchHits = search.getSearchHits();

            //设置一个最后需要返回的实体类集合
            List<T> list = new ArrayList<>();
            //遍历返回的内容进行处理
            for (SearchHit<T> searchHit : searchHits) {
                list.add(searchHit.getContent());
            }
            return list;
        } else {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(0, 10000));
            return null;
        }
    }

    /*------------------------------------------- private 私有方法 ----------------------------------------------*/

    private <T> void getNestQueryBuilder(BoolQueryBuilder boolQueryBuilder, Class<T> clazz, List<BaseEsEntity.MultiLayerRelation> multiLayerQueryList) throws NoSuchFieldException {
        if (null != multiLayerQueryList && !multiLayerQueryList.isEmpty()) {
            for (BaseEsEntity.MultiLayerRelation r : multiLayerQueryList) {
                BoolQueryBuilder nestBoolQuery = QueryBuilders.boolQuery();
                EsMapUtil nestMap = r.getMap();
                getFieldQueryBuilder(nestBoolQuery, clazz, nestMap);
                if (Objects.equals(r.getQueryMode(), BaseEsEntity.MUST)) {
                    boolQueryBuilder.must(nestBoolQuery);
                } else if (Objects.equals(r.getQueryMode(), BaseEsEntity.SHOULD)) {
                    boolQueryBuilder.should(nestBoolQuery);
                } else if (Objects.equals(r.getQueryMode(), BaseEsEntity.FILTER)) {
                    boolQueryBuilder.filter(nestBoolQuery);
                } else if (Objects.equals(r.getQueryMode(), BaseEsEntity.MUST_NOT)) {
                    boolQueryBuilder.mustNot(nestBoolQuery);
                }
                //递归嵌套
                if (!r.getMultiLayerList().isEmpty()) {
                    //处理多层bool查询
                    getNestQueryBuilder(nestBoolQuery, clazz, r.getMultiLayerList());
                }
            }
        }
    }

    /**
     * 构建组合查询(支持权重)
     *
     * @param boolQueryBuilder
     * @param clazz
     * @param queryMap
     */
    @SuppressWarnings("unchecked")
    private <T> void getFieldQueryBuilder(BoolQueryBuilder boolQueryBuilder, Class<T> clazz, Map<String, Object> queryMap) throws NoSuchFieldException {
        if (queryMap != null && !queryMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
                String k = entry.getKey();
                List<Object> vList = new ArrayList<>();
                if (entry.getValue() instanceof List) {
                    vList = (ArrayList) entry.getValue();
                } else {
                    vList.add(entry.getValue());
                }
                FieldType type = EsUtils.getFieldType(clazz, k);
                if (Objects.equals(type, FieldType.Text)) {
                    for (Object o : vList) {
                        if (o instanceof BaseEsEntity.RangeRelation) {
                            BaseEsEntity.RangeRelation<T> v = (BaseEsEntity.RangeRelation<T>) o;
                            //范围查询
                            getRangeBuilder(boolQueryBuilder, k, v);
                        } else if (o instanceof BaseEsEntity.QueryRelation) {
                            BaseEsEntity.QueryRelation<T> v = (BaseEsEntity.QueryRelation<T>) o;
                            if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.must(QueryBuilders.matchQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.must(QueryBuilders.matchQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST_NOT)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.mustNot(QueryBuilders.matchQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.mustNot(QueryBuilders.matchQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.SHOULD)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.should(QueryBuilders.matchQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.should(QueryBuilders.matchQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.FILTER)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.filter(QueryBuilders.matchQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.filter(QueryBuilders.matchQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            }
                        }
                    }
                } else if (Objects.equals(type, FieldType.Keyword)) {
                    for (Object o : vList) {
                        if (o instanceof BaseEsEntity.RangeRelation) {
                            BaseEsEntity.RangeRelation<T> v = (BaseEsEntity.RangeRelation<T>) o;
                            //范围查询
                            getRangeBuilder(boolQueryBuilder, k, v);
                        } else if (o instanceof BaseEsEntity.QueryRelation) {
                            BaseEsEntity.QueryRelation<T> v = (BaseEsEntity.QueryRelation<T>) o;
                            if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.must(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.must(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST_NOT)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.SHOULD)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.should(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.should(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.FILTER)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.filter(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.filter(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            }

                        }
                    }
                } else {
                    for (Object o : vList) {
                        if (o instanceof BaseEsEntity.RangeRelation) {
                            BaseEsEntity.RangeRelation<T> v = (BaseEsEntity.RangeRelation<T>) o;
                            //范围查询
                            getRangeBuilder(boolQueryBuilder, k, v);
                        } else if (o instanceof BaseEsEntity.QueryRelation) {
                            BaseEsEntity.QueryRelation<T> v = (BaseEsEntity.QueryRelation<T>) o;
                            if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.must(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.must(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST_NOT)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.SHOULD)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.should(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.should(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.FILTER)) {
                                if (Objects.isNull(v.getBoostValue())) {
                                    boolQueryBuilder.filter(QueryBuilders.termQuery(k, v.getFieldValue()));
                                } else {
                                    boolQueryBuilder.filter(QueryBuilders.termQuery(k, v.getFieldValue()).boost(v.getBoostValue()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 构建范围查询
     *
     * @param boolQueryBuilder
     * @param k
     * @param v
     */
    private <T> void getRangeBuilder(BoolQueryBuilder boolQueryBuilder, String k, BaseEsEntity.RangeRelation<T> v) {
        if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST)) {
            if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).gt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).gte(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).lt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).lte(v.getFieldMinValue()));
            }
            if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).gt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).gte(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).lt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery(k).lte(v.getFieldMaxValue()));
            }
        } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.MUST_NOT)) {
            if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).gt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).gte(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).lt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).lte(v.getFieldMinValue()));
            }
            if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).gt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).gte(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).lt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(k).lte(v.getFieldMaxValue()));
            }
        } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.SHOULD)) {
            if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).gt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).gte(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).lt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).lte(v.getFieldMinValue()));
            }
            if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).gt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).gte(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).lt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(k).lte(v.getFieldMaxValue()));
            }
        } else if (Objects.equals(v.getQueryMode(), BaseEsEntity.FILTER)) {
            if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).gt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).gte(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).lt(v.getFieldMinValue()));
            } else if (Objects.equals(v.getFieldMinMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).lte(v.getFieldMinValue()));
            }
            if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GT)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).gt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.GTE)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).gte(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LT)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).lt(v.getFieldMaxValue()));
            } else if (Objects.equals(v.getFieldMaxMode(), BaseEsEntity.LTE)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(k).lte(v.getFieldMaxValue()));
            }
        }
    }

    /**
     * 构建排序查询
     *
     * @param beanIdName
     * @param orderMap
     * @param nativeSearchQueryBuilder
     */
    private void getOrderBuilder(String beanIdName, Map<String, Object> orderMap, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (!orderMap.isEmpty()) {
            orderMap.forEach((k, v) -> nativeSearchQueryBuilder.withSorts(SortBuilders.fieldSort(k).order((SortOrder) v)));
        } else {
            //无指定排序字段默认按ID倒序
            //nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(beanIdName).order(SortOrder.DESC));
        }
    }

    /**
     * 构建高亮查询
     *
     * @param highlightFields
     * @param preTags
     * @param postTags
     * @param nativeSearchQueryBuilder
     */
    private void getHighLightBuilder(List<String> highlightFields, String preTags, String postTags, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (null != highlightFields && !highlightFields.isEmpty()) {
            List<HighlightBuilder.Field> highlightTitles = new ArrayList<>();
            for (String title : highlightFields) {
                highlightTitles.add(new HighlightBuilder.Field(title).preTags(preTags).postTags(postTags));
            }
            nativeSearchQueryBuilder.withHighlightFields(highlightTitles.toArray(new HighlightBuilder.Field[0]));
        }
    }

}
