package com.nova.common.utils.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

/**
 * @description: jwt工具类
 * @author: wzh
 * @date: 2022/10/11 17:15
 */
public class JwtUtils {

    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 生成签名,expireTime后过期
     *
     * @return 加密的token
     */
    public static String sign(String userName, String randomStr) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(randomStr);
            return JWT.create()
                    .withClaim("userName", userName)
                    .withExpiresAt(date)
                    .withIssuedAt(new Date())
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String userName, String randomStr) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(randomStr);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userName", userName)
                    .build();
            //效验TOKEN
            verifier.verify(token);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String userName = "wzh";
        String randomStr = "123";

        String token = sign(userName, randomStr);
        System.out.println("token：" + token);

        boolean verify = verify(token, userName, randomStr);
        System.out.println("verify：" + verify);
    }
}
