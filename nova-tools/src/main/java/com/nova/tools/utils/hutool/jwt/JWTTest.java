package com.nova.tools.utils.hutool.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.Test;

public class JWTTest {

    @Test
    public void createHs256Test() {
        byte[] key = "1234567890".getBytes();
        JWT jwt = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "looly")
                .setPayload("admin", true)
                .setExpiresAt(DateUtil.parse("2022-01-01"))
                .setKey(key);

        String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Imxvb2x5IiwiYWRtaW4iOnRydWUsImV4cCI6MTY0MDk2NjQwMH0." +
                "bXlSnqVeJXWqUIt7HyEhgKNVlIPjkumHlAwFY-5YCtk";

        String token = jwt.sign();
        Assert.equals(rightToken, token);

        Assert.isTrue(JWT.of(rightToken).setKey(key).verify());
    }

    @Test
    public void parseTest() {
        String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9." +
                "U2aQkC2THYV9L0fTN-yBBI7gmo5xhmvMhATtu8v0zEA";

        final JWT jwt = JWT.of(rightToken);

        Assert.isTrue(jwt.setKey("1234567890".getBytes()).verify());

        //header
        Assert.equals("JWT", jwt.getHeader(JWTHeader.TYPE));
        Assert.equals("HS256", jwt.getHeader(JWTHeader.ALGORITHM));
        Assert.isNull(jwt.getHeader(JWTHeader.CONTENT_TYPE));

        //payload
        Assert.equals("1234567890", jwt.getPayload("sub"));
        Assert.equals("looly", jwt.getPayload("name"));
        Assert.equals(true, jwt.getPayload("admin"));
    }

    @Test
    public void createNoneTest() {
        JWT jwt = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "looly")
                .setPayload("admin", true)
                .setSigner(JWTSignerUtil.none());

        String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwiYWRtaW4iOnRydWUsIm5hbWUiOiJsb29seSJ9.";

        String token = jwt.sign();
        Assert.equals(token, token);

        Assert.isTrue(JWT.of(rightToken).setSigner(JWTSignerUtil.none()).verify());
    }

    /**
     * 必须定义签名器
     */
    @Test
    public void needSignerTest() {
        JWT jwt = JWT.create()
                .setPayload("sub", "1234567890")
                .setPayload("name", "looly")
                .setPayload("admin", true);

        jwt.sign();
    }

    @Test
    public void verifyTest() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2MjQwMDQ4MjIsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV_op5LoibLkuozlj7ciLCJzeXNfbWVudV8xIiwiUk9MRV_op5LoibLkuIDlj7ciLCJzeXNfbWVudV8yIl0sImp0aSI6ImQ0YzVlYjgwLTA5ZTctNGU0ZC1hZTg3LTVkNGI5M2FhNmFiNiIsImNsaWVudF9pZCI6ImhhbmR5LXNob3AifQ." +
                "aixF1eKlAKS_k3ynFnStE7-IRGiD5YaqznvK2xEjBew";

        final boolean verify = JWT.of(token).setKey(StrUtil.utf8Bytes("123456")).verify();
        Assert.isTrue(verify);
    }
}
