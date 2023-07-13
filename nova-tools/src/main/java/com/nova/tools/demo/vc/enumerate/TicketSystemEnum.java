package com.nova.tools.demo.vc.enumerate;

import java.io.Serializable;


public enum TicketSystemEnum implements Serializable {
    /**
     * 1905
     */
    M1905(6),
    /**
     * 维信
     */
    WEIXIN(7),
    /**
     * 火凤凰
     */
    HUOFENGHUANG(8),
    /**
     * 凤凰云智
     */
    NEWHUOFENGHUANG(21),
    /**
     * 星河
     */
    XINGHE(9),
    /**
     * 鼎新
     */
    DINGXIN(10),
    /**
     * 满天星
     */
    CMTS(13),
    /**
     * 火烈鸟
     */
    HUOLIENIAO(14),
    /**
     * Vista
     */
    VISTA(15),
    /**
     * 晨星
     */
    CHENXING(17),
    /**
     * 大地
     */
    DADIMEDIA(18),
    /**
     * 广电13规范接口
     */
    GUANGDIAN(20),
    /**
     * 威云
     */
    WEIYUN(25),
    /**
     * 指点无线
     */
    ZHIDIANWUXIAN(11),
    /**
     * 天河
     */
    TIANHE(5),

    /**
     * 快购云
     */
    KUAIGOUYUN(26);

    private int value;

    TicketSystemEnum() {
    }

    private TicketSystemEnum(int value) {
        this.value = value;
    }

    public static TicketSystemEnum getTicketSystemEnum(int value) {
        switch (value) {
            case 5:
                return TIANHE;
            case 6:
                return M1905;
            case 7:
                return WEIXIN;
            case 8:
                return HUOFENGHUANG;
            case 9:
                return XINGHE;
            case 10:
                return DINGXIN;
            case 11:
                return ZHIDIANWUXIAN;
            case 13:
                return CMTS;
            case 14:
                return HUOLIENIAO;
            case 15:
                return VISTA;
            case 17:
                return CHENXING;
            case 18:
                return DADIMEDIA;
            case 20:
                return GUANGDIAN;
            case 21:
                return NEWHUOFENGHUANG;
            case 25:
                return WEIYUN;
            case 26:
                return KUAIGOUYUN;
            default:
                return WEIXIN;
        }
    }

    public int getValue() {
        return value;
    }
}
