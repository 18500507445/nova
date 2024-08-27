package com.nova.orm.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.nova.orm.mybatisplus.chapter5.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

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
//autoResultMap json和bean互转生效
@TableName(value = "user", autoResultMap = true)
public class UserFiveDO {
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

    /**
     * 删除表示 默认0
     */
    private Boolean status;

    @TableField(value = "gender")
    private GenderEnum gender;

    /**
     * 联系方式，字段处理成Json
     * todo json字符串和map或者pojo对象互转，类型可以选的很多，fastJson需要项目中有依赖
     */
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private Map<String, String> contact;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

}
