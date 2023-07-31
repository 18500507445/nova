package com.nova.tools.demo.exercise;


import com.nova.tools.demo.entity.Myself;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @description:
 * @author: wzh
 * @date: 2018/12/16 18:50
 */

public class ExceptionTest {

    public static void main(String[] args) {
        try {
            boolean a = Myself.BOOLEAN_TRUE;
            if (a) {
                throw new RuntimeException("手动抛异常创建失败");
            } else {
                System.err.println("程序执行");
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            System.err.println(stringWriter);

            System.err.println(e.getMessage());
        }
    }
}
