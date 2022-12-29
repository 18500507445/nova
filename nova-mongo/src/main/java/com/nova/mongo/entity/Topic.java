package com.nova.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/9/8 21:47
 */
@Document(collection = "tb_topic")
@Data
public class Topic {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 选择题的选项
     */
    private Map<Integer, String> select;

    /**
     * 答案
     */
    private Integer answer;

    /**
     * 题目类型（0：选择题，1：判断题）
     */
    private Integer type;

    /**
     * 题目的学科类型id
     */
    private Long subject;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 相关的图片地址
     */
    private String image;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 逻辑删除
     */
    private Boolean deleted;
}
