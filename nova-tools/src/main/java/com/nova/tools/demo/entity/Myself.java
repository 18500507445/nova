package com.nova.tools.demo.entity;

import cn.hutool.core.date.DateUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 数据类
 * @Author: wangzehui
 * @Date: 2021/10/16 12:00 下午
 */
public class Myself {

    public static final BigDecimal ZERO = BigDecimal.ZERO;

    //int 0
    public static final int INT_ZERO = 0;

    //string-success
    public static final String STRING_SUCCESS = "success";

    public static final String SEAT_CODE = "1_2_3";

    //布尔 true
    public static final Boolean BOOLEAN_TRUE = Boolean.TRUE;

    //布尔 false
    public static final Boolean BOOLEAN_FALSE = Boolean.FALSE;

    /**
     *
     */
    public static final List<People> PEOPLE_LIST = Arrays.asList(
            new People(1, 12, "张三", "傻"),
            new People(2, 16, "赵四", "白"),
            new People(3, 18, "王五", "甜"),
            new People(4, 21, "田六", "胖"),
            new People(5, 25, "孙七", "高")
    );


    /**
     * exercise-list
     */
    public static final List<People> EXERCISE_LIST = Arrays.asList(
            new People(1, 15, "小明", 1, "一组", "", DateUtil.beginOfWeek(new Date()), new BigDecimal("12")),
            new People(2, 16, "小红", 1, "一组", "很高", DateUtil.date(), new BigDecimal("1")),
            new People(3, 17, "小李", 2, "二组", "很胖", DateUtil.beginOfMonth(new Date()), new BigDecimal("5")),
            new People(4, 18, "小王", 3, "三组", "人很好", DateUtil.beginOfYear(new Date()), new BigDecimal("100")),
            new People(5, 25, "小明", 3, "三组", "重名", DateUtil.beginOfHour(new Date()), new BigDecimal("99"))
    );

    //string-arr
    public static final String[] STRING_ARR = {"1,2,3,4,5"};

    //film-arr
    public static final String[] FILM_ARR = {"中国巨幕2K", "4K", "2K", "4D", "ATMOS", "IMAX3D", "巨幕3D", "IMAX", "巨幕", "3D"};

    //int-arr
    public static final int[] INT_ARR = {33, 23, 78, 90, 1, 5};

}
