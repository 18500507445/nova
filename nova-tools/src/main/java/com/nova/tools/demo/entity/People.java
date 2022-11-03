package com.nova.tools.demo.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/6/14 14:09
 */
@Data
@Builder
@ToString
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
