package com.nova.search.elasticsearch.repository;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nova.search.elasticsearch.annotation.EsRepository;
import lombok.Data;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: wzh
 * @description 实体类
 * @date: 2023/07/13 22:56
 */
@Order(Integer.MIN_VALUE)
@Data
//索引名称，默认自动创建
@Document(indexName = "user")
@EsRepository(UserRepository.class)
//分片数量建议20-30G为一片（默认值1片，1副本），从缓冲区刷盘建议：30s，默认1s开销较大
@Setting(shards = 3, refreshInterval = "30s")
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
    @Field(name = "password", type = FieldType.Keyword)
    private String password;

    //身份证
    @Field(name = "idCard", type = FieldType.Keyword)
    private String idCard;

    //性别，1：男，2女
    @Field(name = "sex", type = FieldType.Integer)
    private Integer sex;

    //年龄
    @Field(name = "age", type = FieldType.Integer)
    private Integer age;

    //地址
    @Field(name = "address", type = FieldType.Text, analyzer = "ik_max_word")
    private String address;

    //创建时间
    @Field(name = "createTime", type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    //fastjson序列化格式
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    //jackson时间序列化格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //忽略字段
    @Field(ignoreFields = "bala")
    private String bala;
}
