package com.nova.tools.java8.grow.jdk7;


import com.nova.tools.java8.grow.jdk6.ScriptEngineDemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * try-with-resource
 *
 * @author wzh
 * @date 2018/2/8
 */
class TryWithResource {

    public static void main(String[] args) {
        String path = Objects.requireNonNull(ScriptEngineDemo.class.getResource("/test.js")).getPath();

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
