package com.nova.tools.utils.hutool.crypto;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PemUtilTest {

    @Testpublic
    void readPrivateKeyTest() {
        final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_private_key.pem"));
        Assert.notNull(privateKey);
    }

    @Testpublic
    void readPublicKeyTest() {
        final PublicKey publicKey = PemUtil.readPemPublicKey(ResourceUtil.getStream("test_public_key.csr"));
        Assert.notNull(publicKey);
    }

    @Test
    public void readPemKeyTest() {
        final PublicKey publicKey = (PublicKey) PemUtil.readPemKey(ResourceUtil.getStream("test_public_key.csr"));
        Assert.notNull(publicKey);
    }

    @Test
    public void validateKey() {
        final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_private_key.pem"));
        final PublicKey publicKey = PemUtil.readPemPublicKey(ResourceUtil.getStream("test_public_key.csr"));

        final RSA rsa = new RSA(privateKey, publicKey);
        final String str = "你好，Hutool";//测试字符串

        final String encryptStr = rsa.encryptBase64(str, KeyType.PublicKey);
        final String decryptStr = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
        Assert.equals(str, decryptStr);
    }

    @Test
    public void readECPrivateKeyTest() {
        final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_ec_sec1_private_key.pem"));
        final SM2 sm2 = new SM2(privateKey, null);
        sm2.usePlainEncoding();

        //需要签名的明文,得到明文对应的字节数组
        final byte[] dataBytes = "我是一段测试aaaa".getBytes(StandardCharsets.UTF_8);

        final byte[] sign = sm2.sign(dataBytes, null);
        // 64位签名
        Assert.equals(64, sign.length);
    }

    @Test
    public void readECPrivateKeyTest2() {
        // https://gitee.com/dromara/hutool/issues/I37Z75
        final byte[] d = PemUtil.readPem(FileUtil.getInputStream("d:/test/keys/priv.key"));
        final byte[] publicKey = PemUtil.readPem(FileUtil.getInputStream("d:/test/keys/pub.key"));

        final SM2 sm2 = new SM2(d, publicKey);
        sm2.usePlainEncoding();

        final String content = "我是Hanley.";
        final byte[] sign = sm2.sign(StrUtil.utf8Bytes(content));
        final boolean verify = sm2.verify(StrUtil.utf8Bytes(content), sign);
        Assert.isTrue(verify);
    }

}
