package com.nova.pay.enums;

/**
 * @description: 业务枚举类
 * @author: wzh
 * @date: 2022/10/10 11:53
 */
public enum BusinessEnum {

    DEFAULT(0, "默认"),

    RECHARGE(1, "充值"),

    VIP_MONTH(2, "包月会员"),

    VIP_QUARTER(3, "包季会员"),

    VIP_YEAR(4, "包年会员"),

    AUTO_VIP_MONTH(5, "连续包月会员"),

    AUTO_VIP_QUARTER(6, "连续包季会员"),

    AUTO_VIP_YEAR(7, "连续包年会员"),

    SUBSCRIBE_ONE_WEEK(8, "订阅1周"),

    SUBSCRIBE_TWO_WEEK(9, "订阅2周"),

    SUBSCRIBE_MONTH(10, "订阅1个月"),

    PACKAGE_CARD_WEEK(11, "套餐卡周"),

    PACKAGE_CARD_MONTH(12, "套餐卡月"),

    BUY_PLAN(13, "购买方案"),

    EXCLUSIVE_SILVER_VIP(14, "开通尊享白银会员"),

    EXCLUSIVE_GOLD_VIP(15, "开通尊享黄金会员"),

    EXCLUSIVE_UPGRADE_VIP(16, "尊享会员升级"),

    BIG_DATA(17, "大数据"),

    DATA_MODEL(18, "红单数据模型"),

    MATCH_SILK_BAG(19, "赛事锦囊");

    /**
     * 业务code
     */
    private final int code;

    /**
     * 业务名称
     */
    private final String name;

    BusinessEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BusinessEnum valuesOf(int code) {
        switch (code) {
            case 1:
                return RECHARGE;
            case 2:
                return VIP_MONTH;
            case 3:
                return VIP_QUARTER;
            case 4:
                return VIP_YEAR;
            case 5:
                return AUTO_VIP_MONTH;
            case 6:
                return AUTO_VIP_QUARTER;
            case 7:
                return AUTO_VIP_YEAR;
            case 8:
                return SUBSCRIBE_ONE_WEEK;
            case 9:
                return SUBSCRIBE_TWO_WEEK;
            case 10:
                return SUBSCRIBE_MONTH;
            case 11:
                return PACKAGE_CARD_WEEK;
            case 12:
                return PACKAGE_CARD_MONTH;
            case 13:
                return BUY_PLAN;
            case 14:
                return EXCLUSIVE_SILVER_VIP;
            case 15:
                return EXCLUSIVE_GOLD_VIP;
            case 16:
                return EXCLUSIVE_UPGRADE_VIP;
            case 17:
                return BIG_DATA;
            case 18:
                return DATA_MODEL;
            case 19:
                return MATCH_SILK_BAG;
            default:
                return DEFAULT;
        }
    }
}
