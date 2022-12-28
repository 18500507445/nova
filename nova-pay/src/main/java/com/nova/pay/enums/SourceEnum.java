package com.nova.pay.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @description: 目前yt_source表没有维护，手动维护枚举类
 * @author: wangzehui
 * @date: 2021/5/31 8:12 下午
 */
@Deprecated
public enum SourceEnum {

    CRAZY_RED("疯狂红单", "72", "185"),

    CRAZY_SPORTS("疯狂体育", "78", "77"),

    CRAZY_QUIZ("疯狂竞猜", "0", "87"),

    CRAZY_SPORTS_LIVE("疯狂比分直播", "155", "0"),

    KNOW_BALL_SPORTS("懂球体育", "171", "170"),

    COLORFUL_SPORTS("红彩体育", "198", "0"),

    RED_LIST_DOUBLE_BALL("红单双色球", "199", "0"),

    STRIKE_COMMUNITY("好球社区", "200", "0"),

    HEBEI_QUIZ("河北竞猜", "201", "202"),

    DELIVERY_SHOPKEEPER("配送店主", "204", "203"),

    DELIVERY("配送", "206", "205"),

    GOOD_BALL_LIVE("好球直播", "0", "207"),

    FOOTBALL_SCORE_PRO("足球比分pro", "0", "208"),

    RED_LIST_MEMBER("红单会员", "0", "209"),

    CRAZY_SPORTS_TRADITIONAL("疯狂体育繁体版", "0", "210"),

    DREAM_MATCH("梦幻赛事", "212", "211"),

    FOOTBALL_SCORE("足球比分", "213", "208"),

    LOW_PRICE_PACKAGE("战报低价包", "215", "214");

    /**
     * 包名
     */
    private String packageName;


    /**
     * 安卓code
     */
    private String androidCode;


    /**
     * 苹果code
     */
    private String iosCode;

    SourceEnum(String packageName, String androidCode, String iosCode) {
        this.packageName = packageName;
        this.androidCode = androidCode;
        this.iosCode = iosCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAndroidCode() {
        return androidCode;
    }

    public void setAndroidCode(String androidCode) {
        this.androidCode = androidCode;
    }

    public String getIosCode() {
        return iosCode;
    }

    public void setIosCode(String iosCode) {
        this.iosCode = iosCode;
    }

    public static String androidCode(String packageName) {
        if (StringUtils.isNotBlank(packageName)) {
            return Arrays.stream(values()).filter(sourceEnum -> sourceEnum.getPackageName().equals(packageName)).findAny().get().androidCode;
        }
        return null;
    }

    public static String iosCode(String packageName) {
        if (StringUtils.isNotBlank(packageName)) {
            return Arrays.stream(values()).filter(sourceEnum -> sourceEnum.getPackageName().equals(packageName)).findAny().get().iosCode;
        }
        return null;
    }

    /**
     * 根据code(ios,android)获取name
     *
     * @param code
     * @return
     */
    public static String packageName(String code) {
        if (StringUtils.isNotBlank(code)) {
            return Arrays.stream(values()).filter(sourceEnum -> sourceEnum.getAndroidCode().equals(code) || sourceEnum.getIosCode().equals(code)).findAny().get().packageName;
        } else {
            return "";
        }
    }


}
