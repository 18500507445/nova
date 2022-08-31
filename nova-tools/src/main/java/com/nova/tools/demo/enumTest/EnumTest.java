package com.nova.tools.demo.enumTest;


import com.nova.tools.utils.enumerate.RequestTypeEnum;

/**
 * @author wangzehui
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
