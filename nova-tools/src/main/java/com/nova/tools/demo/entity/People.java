package com.nova.tools.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2019/6/14 14:09
 */
@Data
@SuperBuilder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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

    /**
     * 钱
     */
    private BigDecimal fee;

    private Double doubleFee;

}
