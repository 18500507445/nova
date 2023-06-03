package com.nova.tools.demo.entity;

import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description: 数据类
 * @author: wzh
 * @date: 2021/10/16 12:00 下午
 */
public class Myself {

    public static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * int 0
     */
    public static final int INT_ZERO = 0;

    /**
     * string-success
     */
    public static final String STRING_SUCCESS = "success";

    public static final String SEAT_CODE = "1_2_3";

    /**
     * 布尔 true
     */
    public static final Boolean BOOLEAN_TRUE = Boolean.TRUE;

    /**
     * 布尔 false
     */
    public static final Boolean BOOLEAN_FALSE = Boolean.FALSE;

    /**
     * exercise-list
     */
    public static final List<People> EXERCISE_LIST = Arrays.asList(
            People.builder().id(1).age(15).name("小明").groupId(1).groupName("一组").description("").createTime(DateUtil.beginOfWeek(new Date())).fee(new BigDecimal(12)).doubleFee(12.01).build(),
            People.builder().id(2).age(16).name("小红").groupId(1).groupName("一组").description("很高").createTime(DateUtil.date()).fee(new BigDecimal(1)).doubleFee(11.05).build(),
            People.builder().id(3).age(17).name("小李").groupId(2).groupName("二组").description("很胖").createTime(DateUtil.beginOfMonth(new Date())).fee(new BigDecimal(5)).doubleFee(4.66).build(),
            People.builder().id(4).age(18).name("小王").groupId(3).groupName("三组").description("人很好").createTime(DateUtil.beginOfYear(new Date())).fee(new BigDecimal(100)).doubleFee(13.99).build(),
            People.builder().id(5).age(25).name("小明").groupId(3).groupName("三组").description("重名").createTime(DateUtil.beginOfHour(new Date())).fee(new BigDecimal(99)).doubleFee(88.88).build()
    );

    /**
     * string-arr
     */
    public static final String[] STRING_ARR = {"1,2,3,4,5"};

    /**
     * film-arr
     */
    public static final String[] FILM_ARR = {"中国巨幕2K", "4K", "2K", "4D", "ATMOS", "IMAX3D", "巨幕3D", "IMAX", "巨幕", "3D"};

    /**
     * int-arr
     */
    public static final int[] INT_ARR = {33, 23, 78, 90, 1, 5};

}
