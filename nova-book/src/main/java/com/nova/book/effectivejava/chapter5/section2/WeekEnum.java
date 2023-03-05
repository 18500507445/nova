package com.nova.book.effectivejava.chapter5.section2;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:22
 */
enum WeekEnum {

    TWO(2, "星期二"),

    ONE(1, "星期一"),

    FOUR(4, "星期四"),

    THREE(3, "星期三"),

    FIVE(5, "星期五"),

    SIX(6, "星期六"),

    SEVEN(7, "星期天");

    private Integer id;

    private String name;

    WeekEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static WeekEnum valuesOf(int index) {
        switch (index) {
            case 1:
                return ONE;
            default:
                return null;
        }
    }
}
