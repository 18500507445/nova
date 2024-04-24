package com.nova.common.utils.security;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.nova.common.utils.id.IdUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * @description: 请求URL地址加密解密工具类
 * @author: wzh
 * @date: 2021/11/21 10:56
 */
@Slf4j(topic = "SecurityUtil")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    /**
     * app:ios和android的key
     * SfNJN1O69Zs1ekjB
     */
    private static final String SECRET_KEY = "secret_key";

    /**
     * h5特有的key
     */
    private static final String H5_SECRET_KEY = "h5_secret_key";

    public static Map<String, String> KEY_MAP = new HashMap<>();

    static {
        KEY_MAP.put("android", "android");
        KEY_MAP.put("ios", "ios");
        KEY_MAP.put("h5", "h5");
    }

    public synchronized static String decryptAllPara(String params, String clientType) {
        String decryptvalue = "";
        try {
            if (StrUtil.isNotBlank(clientType)) {
                if (StrUtil.equals("h5", clientType)) {
                    decryptvalue = decrypt(params.getBytes(StandardCharsets.UTF_8), H5_SECRET_KEY);
                } else if (ArrayUtil.contains(new String[]{"android", "ios"}, clientType)) {
                    decryptvalue = decrypt(URLDecoder.decode(params, "utf-8").getBytes(StandardCharsets.UTF_8), SECRET_KEY);
                }
            } else {
                decryptvalue = URLDecoder.decode(decrypt(params.getBytes(StandardCharsets.UTF_8)), "utf-8");
            }
            decryptvalue = URLDecoder.decode(decryptvalue, "utf-8");
        } catch (Exception e) {
            log.error("decryptAllPara====>解密全部参数失败:", e);
        }
        return decryptvalue;
    }

    /**
     * 解密
     *
     * @param src
     */
    public static String decrypt(byte[] src) throws Exception {
        SecretKey sKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.DECRYPT_MODE, sKey);
        byte[] b = Base64Utils.decodeFromString(new String(src, StandardCharsets.UTF_8).trim());
        byte[] result = ci.doFinal(b);
        return new String(result, StandardCharsets.UTF_8);
    }


    /**
     * 解密
     *
     * @param src
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
    public static String encrypt(byte[] src) throws Exception {
        SecretKey sKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] b = ci.doFinal(src);
        return Base64Utils.encodeToString(b);
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


    /**
     * 编码：base64
     * 位数：128位
     * 模式：ECB
     * 补码：PKCS5
     * 偏移量：无
     */
    @Test
    public void demoA() {
        try {
            final String key = "SfNJN1O69Zs1ekjB";
            Map<String, String> hashMap = new HashMap<>(16);
            hashMap.put("wzh", "123");
            String param = JSONUtil.toJsonStr(hashMap);
            System.err.println("请求加密param：" + param);
            String secret = URLEncoder.encode(param, "utf-8");
            System.err.println("URLenCode：" + secret);
            String str = SecurityUtils.encrypt(secret.getBytes(StandardCharsets.UTF_8), key);
            System.err.println("加密后str：" + str);
            String encode = URLEncoder.encode(str, "utf-8");
            System.err.println("(这个要传给服务器了)---加密后再Url.enCode:" + encode);

            System.err.println("------------------------------------------------------");
            String accessSecretData = URLDecoder.decode(encode, "utf-8");
            System.err.println("accessSecretData = " + accessSecretData);
            String result = SecurityUtils.decrypt(accessSecretData.getBytes(StandardCharsets.UTF_8), key);
            result = URLDecoder.decode(result, "utf-8");
            System.err.println("服务器解密后str:" + result);
        } catch (Exception e) {
            log.error("异常信息:", e);
        }
    }

    @Test
    @SneakyThrows
    public void demoB() throws UnsupportedEncodingException {
        final String key = "SfNJN1O69Zs1ekjB";
        String supplierId = "123";
        Integer pushType = 1;
        String snowId = IdUtils.snowId();
        Long timestamp = System.currentTimeMillis();
        //键升序
        Map<String, Object> hashMap = new TreeMap<>();
        hashMap.put("pushType", pushType);
        hashMap.put("supplierId", supplierId);
        hashMap.put("snowId", snowId);
        hashMap.put("timestamp", timestamp);
        String param = JSONUtil.toJsonStr(hashMap);
        String encode = URLEncoder.encode(param, "utf-8");
        System.out.println("param = " + param);
        // 构建
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, key.getBytes());
        String sign = aes.encryptBase64(encode);
        System.err.println("encryptBase64 = " + sign);

        String jsonData = SecurityUtils.decrypt(sign.getBytes(StandardCharsets.UTF_8), key);
        String result = URLDecoder.decode(jsonData, "utf-8");
        System.out.println("result = " + result);

        ThreadUtil.sleep(5000);
        Map<String, Object> signData = JSONObject.parseObject(result, new TypeReference<Map<String, Object>>() {
        });
        Long signTimestamp = MapUtil.getLong(signData, "timestamp");
        if (System.currentTimeMillis() - signTimestamp > 5000) {
            System.err.println("超过5秒");
        } else {
            System.out.println("成功");
        }
    }
}