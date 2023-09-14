package com.nova.common.utils.security;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: jwt工具类
 * @author: wzh
 * @date: 2022/10/11 17:15
 */
public class JwtUtils {

    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    private static final String SECRET = "abc";

    /**
     * 生成签名,expireTime后过期
     *
     * @return 加密的token
     */
    public static String sign(String json) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create().withClaim("json", json).withExpiresAt(date).withIssuedAt(new Date()).sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, Claim> parse(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.require(algorithm).build().verify(token).getClaims();
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        return JWT.decode(token).getExpiresAt().before(now);
    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String json) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("json", json).build();
            //效验TOKEN
            verifier.verify(token);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Map<String, String> hashMap = new HashMap<>(16);
        hashMap.put("id", "1");
        hashMap.put("name", "wzh");

        String jsonStr = JSONUtil.toJsonStr(hashMap);

        String token = sign(jsonStr);
        System.err.println("token：" + token);

        boolean tokenExpired = isTokenExpired(token);
        System.err.println("tokenExpired = " + tokenExpired);

        boolean verify = verify(token, jsonStr);
        System.err.println("verify：" + verify);

        Map<String, Claim> parse = parse(token);
        String result = parse.get("json").asString();
        System.out.println("result = " + result);

    }
}
