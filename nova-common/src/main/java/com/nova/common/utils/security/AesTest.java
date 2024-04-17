package com.nova.common.utils.security;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author: wzh
 * @description: AES测试类
 * @date: 2024/04/16 15:59
 */
public class AesTest {

    /**
     * 解密
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] src, String key) throws Exception {
        SecretKey sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.DECRYPT_MODE, sKey);
        byte[] b = Base64Utils.decodeFromString(new String(src, StandardCharsets.UTF_8).trim());
        byte[] result = ci.doFinal(b);
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * @param src
     */
    public static String encrypt(byte[] src, String key) throws Exception {
        SecretKey sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] b = ci.doFinal(src);
        return Base64Utils.encodeToString(b);
    }

}
