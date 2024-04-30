package com.nova.search.elasticsearch.manage;

import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @description: 功能性字段(非mapping结构字段)
 */
@Data
@Deprecated
public class BaseEsEntity {

    /**
     * 文档 必须 匹配这些条件才能被查询到。相当于sql中的and
     */
    public static String MUST = "must";

    /**
     * 文档 必须不 匹配这些条件才能被查询到。相当于sql中的 not
     */
    public static String MUST_NOT = "must_not";

    /**
     * 如果满足这些语句中的任意语句，将增加 _score ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。相当于sql中的or
     */
    public static String SHOULD = "should";

    /**
     * 必须 匹配，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档
     */
    public static String FILTER = "filter";

    /**
     * 至少匹配一项should子句
     */
    public static String MINIMUM_SHOULD_MATCH = "minimum_should_match";

    public static String COUNT = "count";

    public static String SUM = "sum";

    /**
     * 多字段排序查询
     */
    public EsMapUtil orderMap;

    /**
     * 分页查询，页码、页大小
     */
    public Integer pageIndex;
    public Integer pageSize;

    /**
     * 游标分页ID
     */
    public String scrollId;

    /**
     * 游标分页ID有效期 单位：毫秒
     */
    public static Long scrollIdExpireTime = 1000 * 60 * 2L;

    /**
     * 游标分页ID最小有效期 单位：毫秒
     */
    public static Long scrollIdMinExpireTime = 1000L;

    /**
     * 高亮查询
     */
    public List<String> highlightFields;

    public String preTags;

    public String postTags;

    /**
     * 字段查询
     */
    public EsMapUtil fieldQueryMap;

    /**
     * 聚合查询，当前只支持单个字段分组聚合count与sum,只针对keyword类型字段有效
     */
    public EsMapUtil aggregationMap;

    /**
     * 多层(bool)查询
     */
    public List multiLayerQueryList;

    /**
     * 范围查询常量
     */
    public static String GT = "gt";
    public static String GTE = "gte";
    public static String LT = "lt";
    public static String LTE = "lte";

    //范围
    @Data
    public static class RangeRelation<T> {

        T fieldMinValue;

        String fieldMinMode;

        T fieldMaxValue;

        String fieldMaxMode;

        String queryMode;

        public RangeRelation(T fieldMinValue, String fieldMinMode, T fieldMaxValue, String fieldMaxMode, String queryMode) {
            this.fieldMinValue = fieldMinValue;
            this.fieldMinMode = fieldMinMode;
            this.fieldMaxValue = fieldMaxValue;
            this.fieldMaxMode = fieldMaxMode;
            this.queryMode = queryMode;
        }
    }

    //查询
    @Data
    public static class QueryRelation<T> {

        T fieldValue;

        String queryMode;

        //权重
        Float boostValue;

        public QueryRelation(T fieldValue, String queryMode) {
            this.fieldValue = fieldValue;
            this.queryMode = queryMode;
        }

        public QueryRelation(T fieldValue, String queryMode, Float boostValue) {
            this.fieldValue = fieldValue;
            this.queryMode = queryMode;
            this.boostValue = boostValue;
        }
    }

    //多层嵌套
    @Data
    public static class MultiLayerRelation {

        String queryMode;

        EsMapUtil map;

        List<BaseEsEntity.MultiLayerRelation> multiLayerList;

        public MultiLayerRelation(String queryMode, EsMapUtil map) {
            this.queryMode = queryMode;
            this.map = map;
        }

        public MultiLayerRelation(String queryMode, EsMapUtil map, List<MultiLayerRelation> multiLayerList) {
            this.queryMode = queryMode;
            this.map = map;
            this.multiLayerList = multiLayerList;
        }
    }
}