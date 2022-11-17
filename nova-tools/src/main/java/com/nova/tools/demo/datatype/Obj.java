package com.nova.tools.demo.datatype;


import com.nova.tools.demo.entity.Myself;

/**
 * @author wangzehui
 * @date 2018/10/19 13:47
 */

public class Obj {

    public static void main(String[] args) {
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
}
