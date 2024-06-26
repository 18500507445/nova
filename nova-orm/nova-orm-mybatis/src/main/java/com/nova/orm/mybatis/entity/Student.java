package com.nova.orm.mybatis.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/31 20:49
 */
@Data
public class Student {

    private Long id;

    private String name;

    private int age;

    private Date createTime;

}
