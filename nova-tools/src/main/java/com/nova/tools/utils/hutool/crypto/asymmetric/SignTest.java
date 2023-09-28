package com.nova.tools.utils.hutool.crypto.asymmetric;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名单元测试
 *
 * @author looly
 */
public class SignTest {

    @Test
    public void signAndVerifyUseKeyTest() {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyBc/2KYOLGxe4PND2HB/lTEiKK6kmyGGQVp07W0MsfulMXlNnos6FRpaFIvJeq34OAXJUBpNjhN6J/DGPoXRKJ38vuggHhuFUo0Yt9Ak7WUnv0bATOD8ffOyj79kO459uVZ9iCjnzdwgbR3FSlAg8VIwhR5O/G3L6KoMGUIDM3Xbg/J9v3Rv282H9o44KOUJ4mlr/I7Ta3pDSw4JBVnysLofQ9dOYT+/fR74at2OTtzXbD6hZfpEi7u/qyksy/tKlTbJXWQP05nWYrUFptjABy8OV5r27cdoKyTWwu8jusntuRQOCQjZZ06cwGGlpO7ds0NDyhH/wDQCT0FxfY1M9wIDAQAB";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDIFz/Ypg4sbF7g80PYcH+VMSIorqSbIYZBWnTtbQyx+6UxeU2eizoVGloUi8l6rfg4BclQGk2OE3on8MY+hdEonfy+6CAeG4VSjRi30CTtZSe/RsBM4Px987KPv2Q7jn25Vn2IKOfN3CBtHcVKUCDxUjCFHk78bcvoqgwZQgMzdduD8n2/dG/bzYf2jjgo5QniaWv8jtNrekNLDgkFWfKwuh9D105hP799Hvhq3Y5O3NdsPqFl+kSLu7+rKSzL+0qVNsldZA/TmdZitQWm2MAHLw5Xmvbtx2grJNbC7yO6ye25FA4JCNlnTpzAYaWk7t2zQ0PKEf/ANAJPQXF9jUz3AgMBAAECggEBAK79xlTPRW4MiR3rItbT7ICqK9mwgz05IJVfawuKZ6PxFRQjpoKV+QIjvcxiRJ8KLx+vz9RCbLlox1jO9lIP7lC7eyPrFsu9/eeohombe/Aym4RpeBzEV44/51ZYaGy9va6tubOvMquTbLiBZN4NHgJvgweo7iNOUxr0PQiJ9wq06JmSOPUOkARDUS+KdPZZ99qm6ainm9gfhTmOCdmsQ6cGKreNEdSWWojS+njJIXnh/BQjJ+4qN3S/KHAYHcULg6KzS4QE0EJGiEvea03JliLJoWq5uHQFUewSCp9orjzUM+/GC9Qr5Rbbr+BHA95FbzKuO+JkK37j5KcB3R6dvoECgYEA4yBRf3mZ2oYpalAxOUTc9WO+dwdmYoRopkcGiZIF7z0sWVR0LAprD4Ts+aufp6QwfjC7gC16bqTQUlPuGpx8sKYgErji6AFNWyKHULuM0Q/+kBmRgibA748heRLbreN+8E42aZzRpPik+ZVwB+9+fsMuc68HycXRNe5VSSRIp+cCgYEA4YcUfBr6WBPlzVkPK3HR4g6C6wayU9qZ7IOYSRYBXC0sCgOl6Rt/WJdbBK33n95hJHJ9KYpQwHMjHBcSavlEEdb5fivxtZLF7BAbc5Ut3ZSWuIBXTSk53NAXobbr+CpuBX4wE8C9g8e4dC5BAuUwR4hz7+wERV1iKD/y3+aFUHECgYBXqpHKclVAU6iIsItPX5Up4mYR63lJbXI3PBL55rMDtMj5ce+lUCAizVvqwqPNQUK2cnjs8KDhNBx0qpCtFNnMFVMr5tEuW9JP6vPv5XRk/Zd0Z2oSooK19Lkm0nCYncbRWl8GRNFmq9KBw3yKk5Zvt0amwFhxOTmXZ3bj1G124QKBgC8+nfL1r9X9rRlce8oNGPuzMIfNqlPYqoAafS9qVbSanewnIX14zi+f3WhYjVRHQLBi74dhO1Nli0haKPiR6UmXcEKXMBZqfd3a7fVDng0aEIzsDd3TMhTo3tp4uGwPb2blLWsl6E2P7DsiaWoS2w9RLYBoP+jMDEEETHvNDcZxAoGACp2xgJnaMF7bNEIm1RgVfhovdLeWL9XjMUSkSOsQaRMLevwkQo+z2Ah6SfbfcvCOPuoICQG6SSbI7nrdt/MU334f1kcpRaIKearuUER/1iYOV1WTi8s9c7t0lif2/44gldiAN45x+1lSz48zivBTSrDwUt9kc2nCeYLP6eEfrQ0=";

        String content = "googlePay";
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, privateKey, null);
        // 签名
        byte[] signed = sign.sign(content.getBytes());
        sign = SecureUtil.sign(SignAlgorithm.SHA1withRSA, null, publicKey);
        // 验证签名
        boolean verify = sign.verify(content.getBytes(), signed);
        System.err.println(verify);
    }

    @Test
    public void signAndVerifyTest() {
        signAndVerify(SignAlgorithm.NONEwithRSA);
        signAndVerify(SignAlgorithm.MD2withRSA);
        signAndVerify(SignAlgorithm.MD5withRSA);

        signAndVerify(SignAlgorithm.SHA1withRSA);
        signAndVerify(SignAlgorithm.SHA256withRSA);
        signAndVerify(SignAlgorithm.SHA384withRSA);
        signAndVerify(SignAlgorithm.SHA512withRSA);

        signAndVerify(SignAlgorithm.NONEwithDSA);
        signAndVerify(SignAlgorithm.SHA1withDSA);

        signAndVerify(SignAlgorithm.NONEwithECDSA);
        signAndVerify(SignAlgorithm.SHA1withECDSA);
        signAndVerify(SignAlgorithm.SHA1withECDSA);
        signAndVerify(SignAlgorithm.SHA256withECDSA);
        signAndVerify(SignAlgorithm.SHA384withECDSA);
        signAndVerify(SignAlgorithm.SHA512withECDSA);
    }

    /**
     * 测试各种算法的签名和验证签名
     *
     * @param signAlgorithm 算法
     */
    private void signAndVerify(SignAlgorithm signAlgorithm) {
        byte[] data = StrUtil.utf8Bytes("我是一段测试ab");
        Sign sign = SecureUtil.sign(signAlgorithm);

        // 签名
        byte[] signed = sign.sign(data);

        // 验证签名
        boolean verify = sign.verify(data, signed);
        Assert.isTrue(verify);
    }

    /**
     * 测试MD5withRSA算法的签名和验证签名
     */
    @Test
    public void signAndVerifyTest2() {
        String str = "wx2421b1c4370ec43b 支付测试 JSAPI支付测试 10000100 1add1a30ac87aa2db72f57a2375d8fec http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php oUpF8uMuAJO_M2pxb1Q9zNjWeS6o 1415659990 14.23.150.211 1 JSAPI 0CB01533B8C1EF103065174F50BCA001";
        byte[] data = StrUtil.utf8Bytes(str);
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);

        // 签名
        byte[] signed = sign.sign(data);

        // 验证签名
        boolean verify = sign.verify(data, signed);
        Assert.isTrue(verify);
    }

    @Test
    public void signParamsTest() {
        Map<String, String> build = MapUtil.builder(new HashMap<String, String>())
                .put("key1", "value1")
                .put("key2", "value2").build();

        String sign1 = SecureUtil.signParamsSha1(build);
        Assert.equals("9ed30bfe2efbc7038a824b6c55c24a11bfc0dce5", sign1);
        String sign2 = SecureUtil.signParamsSha1(build, "12345678");
        Assert.equals("944b68d94c952ec178c4caf16b9416b6661f7720", sign2);
        String sign3 = SecureUtil.signParamsSha1(build, "12345678", "abc");
        Assert.equals("edee1b477af1b96ebd20fdf08d818f352928d25d", sign3);
    }

    /**
     * 测试MD5withRSA算法的签名和验证签名
     */
    @Test
    public void signAndVerifyPSSTest() {
        String str = "wx2421b1c4370ec43b 支付测试 JSAPI支付测试 10000100 1add1a30ac87aa2db72f57a2375d8fec http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php oUpF8uMuAJO_M2pxb1Q9zNjWeS6o 1415659990 14.23.150.211 1 JSAPI 0CB01533B8C1EF103065174F50BCA001";
        byte[] data = StrUtil.utf8Bytes(str);
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA_PSS);

        // 签名
        byte[] signed = sign.sign(data);

        // 验证签名
        boolean verify = sign.verify(data, signed);
        Assert.isTrue(verify);
    }
}
