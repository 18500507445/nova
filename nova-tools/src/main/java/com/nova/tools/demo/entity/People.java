package com.nova.tools.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/6/14 14:09
 */
@Data
public class People {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 时间
     */
    public Date createTime;

    public People() {

    }

    public People(Integer id, Integer age, String name, String description) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.description = description;
    }

    public People(Integer id, Integer age, String name, Integer groupId, String groupName, String description, Date createTime) {
        this.id = id;
        this.age = age;
        this.groupId = groupId;
        this.groupName = groupName;
        this.name = name;
        this.description = description;
        this.createTime = createTime;
    }
}
