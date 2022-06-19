package com.nova.tools.demo.datatype;


import com.nova.tools.demo.entity.Myself;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/4/22 10:15
 */

public class TestBoolean {

    private static Boolean valueOf(boolean b) {
        return b ? Myself.BOOLEAN_TRUE : Myself.BOOLEAN_FALSE;
    }

    public static void main(String[] args) {
        Boolean aBoolean = TestBoolean.valueOf(false);
        System.out.println(aBoolean);
    }

}
