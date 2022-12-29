package com.nova.tools.java8.grow.jdk7;


import com.nova.tools.java8.grow.jdk6.ScriptEngineDemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * try-with-resource
 *
 * @author biezhi
 * @date 2018/2/8
 */
public class TryWithResource {

    public static void main(String[] args) {
        String path = ScriptEngineDemo.class.getResource("/test.js").getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String str = br.readLine();
            while (null != str) {
                System.out.println(str);
                str = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
