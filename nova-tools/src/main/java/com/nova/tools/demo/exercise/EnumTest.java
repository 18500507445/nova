package com.nova.tools.demo.exercise;


/**
 * @author wzh
 * @date 2018/10/24 14:06
 */

public class EnumTest {

    public static void main(String[] args) {

        System.out.println(RequestTypeEnum.BUYTICKET);

        System.out.println(RequestTypeEnum.valuesOf(3));

        System.out.println(RequestTypeEnum.BUYTICKET.getCode());

        System.out.println(RequestTypeEnum.BUYTICKET.getName());

    }
}

enum RequestTypeEnum {

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
