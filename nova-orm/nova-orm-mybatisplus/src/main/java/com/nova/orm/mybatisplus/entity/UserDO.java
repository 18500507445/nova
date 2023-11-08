package com.nova.orm.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: wzh
 * @description 用户数据库对象
 * @date: 2023/06/15 19:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
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
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    @TableField("user_name")
    private String userName;

    @TableField("`desc`")
    private String desc;

    @TableField(select = false)
    private String hide;

    @TableField(exist = false)
    private Boolean online;

}
