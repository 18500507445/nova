package com.nova.tools.demo.enumTest;

/**
 * Created by dongliu on 2017/7/6.
 */
public enum RequestTypeEnum {
    LOCKSEAT(1, "锁座"),
    UNLOCKSEAT(2, "解锁"),
    BUYTICKET(3, "购票"),
    REFUNDTICKET(4, "退票"),
    OFFINEBUY(5, "会员卡扣款"),
    OFFINEREFUND(6, "会员卡退款");

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    RequestTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public static RequestTypeEnum valuesOf(int index) {
        switch (index) {
            case 1:
                return LOCKSEAT;
            case 2:
                return UNLOCKSEAT;
            case 3:
                return BUYTICKET;
            case 4:
                return REFUNDTICKET;
            case 5:
                return OFFINEBUY;
            case 6:
                return OFFINEREFUND;
            default:
                return null;
        }
    }

}
