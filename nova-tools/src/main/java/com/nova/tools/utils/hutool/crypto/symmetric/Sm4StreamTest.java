package com.nova.tools.utils.hutool.crypto.symmetric;

import cn.hutool.crypto.symmetric.SM4;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * https://gitee.com/dromara/hutool/issues/I4EMST
 */
public class Sm4StreamTest {

    private static final SM4 sm4 = new SM4();

    private static final boolean IS_CLOSE = false;

    @Test
    public void sm4Test() {
        String source = "d:/test/sm4_1.txt";
        String target = "d:/test/sm4_2.data";
        String target2 = "d:/test/sm4_3.txt";
        encrypt(source, target);
        decrypt(target, target2);
    }

    public static void encrypt(String source, String target) {
        try (InputStream input = new FileInputStream(source);
             OutputStream out = new FileOutputStream(target)) {
            sm4.encrypt(input, out, IS_CLOSE);
            System.err.println("============encrypt end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String source, String target) {
        try (InputStream input = new FileInputStream(source);
             OutputStream out = new FileOutputStream(target)) {
            sm4.decrypt(input, out, IS_CLOSE);
            System.err.println("============decrypt end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
