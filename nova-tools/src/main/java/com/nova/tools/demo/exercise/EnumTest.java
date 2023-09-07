package com.nova.tools.demo.exercise;


import lombok.Getter;

/**
 * @author wzh
 * @date 2018/10/24 14:06
 */

public class EnumTest {

    public static void main(String[] args) {

        System.err.println(RequestTypeEnum.BUYTICKET);

        System.err.println(RequestTypeEnum.valuesOf(3));

        System.err.println(RequestTypeEnum.BUYTICKET.getCode());

        System.err.println(RequestTypeEnum.BUYTICKET.getName());

    }
}

@Getter
enum RequestTypeEnum {

    LOCKSEAT(1, "锁座"),

    UNLOCKSEAT(2, "解锁"),

    BUYTICKET(3, "购票"),

    REFUNDTICKET(4, "退票"),

    OFFINEBUY(5, "会员卡扣款"),

    OFFINEREFUND(6, "会员卡退款");

    private final int code;

    private final String name;

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
