package com.nova.orm.esplus.repository;

import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.Settings;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldStrategy;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

@Settings(shardsNum = 2, replicasNum = 2)
@IndexName(value = "document")
@Data
public class Document {
    /**
     * es中的唯一id
     */
    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    /**
     * 文档标题
     */
    private String title;

    //分数
    private Integer score;

    // 场景一:标记es中不存在的字段
    @IndexField(exist = false)
    private String notExistsField;

    // 场景二:更新时,此字段非空字符串才会被更新
    @IndexField(strategy = FieldStrategy.NOT_EMPTY)
    private String creator;

    // 场景三: 指定fieldData
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String filedData;

    // 场景四:自定义字段名
    @IndexField("wu-la")
    private String ula;

    // 场景五:支持日期字段在es索引中的format类型
    @IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private String gmtCreate;

    // 场景六:支持指定字段在es索引中的分词器类型
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;

    // 场景七：支持指定字段在es的索引中忽略大小写,以便在term查询时不区分大小写,仅对keyword类型字段生效,es的规则,并非框架限制.
    @IndexField(fieldType = FieldType.KEYWORD, ignoreCase = true)
    private String caseTest;

    // 场景八:支持稠密向量类型 稠密向量类型,dims必须同时指定,非负 最大为2048
    @IndexField(fieldType = FieldType.DENSE_VECTOR, dims = 3)
    private double[] vector;

    // 场景九:支持复制字段,复制当前字段至指定字段,支持将多个字段值复制到同一字段,与原生用法一致
    @IndexField(copyTo = "creator")
    private String copiedField;
}
