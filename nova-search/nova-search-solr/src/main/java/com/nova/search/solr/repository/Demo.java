package com.nova.search.solr.repository;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

/**
 * @author: wzh
 * @description: 实体类
 * @date: 2025/10/30 15:05
 */
@Data
public class Demo {

    /**
     * 自增id
     */
    @Field("db_id")
    private String id;

    @Field("sku_id")
    private String skuId;
}
