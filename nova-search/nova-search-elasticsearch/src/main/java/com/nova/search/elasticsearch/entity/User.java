package com.nova.search.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: wzh
 * @description 实体类
 * @date: 2023/07/13 22:56
 */
@Data
@Document(indexName = "user")
public class User implements Serializable {

    /**
     * 必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
     */
    @Id
    private Long id;

    /**
     * type : 字段数据类型。默认情况下，SpringData Elasticsearch 会根据字段的Java类型自动推断数据类型。
     * analyzer : 分词器类型 ik_max_word最大化分词，会将文本做最细粒度的拆分；ik_smart智能分词，会做最粗粒度的拆分
     * index : 是否索引(默认:true)
     * Keyword : 短语，不进行分词
     */
    @Field(name = "username", type = FieldType.Text, analyzer = "ik_max_word")
    private String userName;

    //密码1
    private String password;

    //密码2
    private String passwordTwo;

    //创建时间
    @Field(name = "createTime", type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //忽略字段
    @Transient
    private String bala;
}
