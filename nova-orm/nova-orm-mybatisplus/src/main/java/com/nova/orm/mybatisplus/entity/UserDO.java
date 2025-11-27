package com.nova.orm.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: wzh
 * @description: 用户数据库对象
 * @date: 2023/06/15 19:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "user")
public class UserDO {

    //变量名成：private String name，属性：getName -> Name -> name

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     * mybatis-plus在修改数据库表的时候会自动忽略掉值为null的字段，所以需要使用注解
     */
    @TableField(updateStrategy = FieldStrategy.DEFAULT)
    private String email;

    @TableField("user_name")
    private String userName;

    @TableField("`desc`")
    private String desc;

    @TableField(select = false)
    private String hide;

    //我们表明这个字段在数据库表中没有对应的列
    @TableField(exist = false)
    private Boolean online;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
