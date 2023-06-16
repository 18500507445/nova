package com.nova.mybatisplus.entity;

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
@TableName("user")
public class UserDO {

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
     */
    private String email;

    @TableField("user_name")
    private String userName;

    @TableField("`desc`")
    private String desc;

    @TableField(select = false)
    private String hide;

    @TableField(exist = false)
    private Boolean online;

    /**
     * 变量名成：private String name
     *
     * 属性：getName -> Name -> name
     */
}
