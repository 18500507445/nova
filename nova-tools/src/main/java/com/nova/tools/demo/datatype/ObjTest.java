package com.nova.tools.demo.datatype;


import com.nova.tools.demo.entity.Myself;
import org.junit.jupiter.api.Test;

/**
 * @author wangzehui
 * @date 2018/10/19 13:47
 */

public class ObjTest {

    @Test
    public void testObj() {
        String[] str1 = Myself.STRING_ARR;
        System.out.println("str1地址" + str1);

        String[] str2 = str1.clone();
        System.out.println("str2地址" + str2);

        String[] str3 = Myself.STRING_ARR;

        for (String s : str1) {
            System.out.println(s);
        }

        for (String s : str2) {
            System.out.println(s);
        }

        System.out.println(str1.hashCode());

        System.out.println(str2.hashCode());

        System.out.println("str1和str2值是否相等" + str1.equals(str2));

        System.out.println(str1 == str2);


        System.out.println("----------------------------------------------------------");
        String a = "1";
        String b = "1";
        System.out.println(a.equals(b));
        System.out.println(a == b);
        System.out.println("a" + a.hashCode());
        System.out.println("b" + b.hashCode());
    }

    @Test
    public void testBoolean() {
        /**
         * 条件都为true，结果为true
         * 一假则假
         */
        boolean a = true;
        boolean b = true;
        boolean c = (a = (1 == 2)) && (b = (1 == 2));
        System.out.println("短路和判断" + c);

        /**
         * 条件之一为true，结果为true
         * 一真则真
         */
        boolean e = false;
        boolean f = false;

        boolean g = (e = (1 == 1)) || (f = (1 == 1));
        System.out.println("短路或判断" + g);
    }

    private static Boolean valueOf(boolean b) {
        return b ? Myself.BOOLEAN_TRUE : Myself.BOOLEAN_FALSE;
    }

    @Test
    public void testBooleanTwo(){
        Boolean aBoolean = ObjTest.valueOf(false);
        System.out.println(aBoolean);
    }

}
