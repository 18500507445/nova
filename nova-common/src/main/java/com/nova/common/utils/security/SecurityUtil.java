package com.nova.common.utils.security;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: SecurityUtil.java
 * @description: 请求URL地址加密解密工具类
 * @author: wzh
 * @date: 2021/11/21 10:56
 */
public class SecurityUtil {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * app:ios和android的key
     * SfNJN1O69Zs1ekjB
     */
    private static final String SECRET_KEY = "SfNJN1O69Zs1ekjB";

    /**
     * h5特有的key
     */
    private static final String H5_SECRET_KEY = "LbskjQt8UuYysnGl";

    public static Map<String, String> KEY_MAP = new HashMap<>();

    static {
        KEY_MAP.put("android", "GfQB6pIrPzGZPYzJ");
        KEY_MAP.put("ios", "FMnb8FVDjMX4Pgwd");
        KEY_MAP.put("h5", "NrRtQPDQDxv1h6NZ");
    }

    public synchronized static String decryptAllPara(String params, String clientType) {
        String decryptvalue = "";
        try {
            if (StringUtils.isNotBlank(clientType)) {
                if (StringUtils.equals("h5", clientType)) {
                    decryptvalue = Decrypt(params.getBytes(StandardCharsets.UTF_8), H5_SECRET_KEY);
                } else if (ArrayUtils.contains(new String[]{"android", "ios"}, clientType)) {
                    decryptvalue = Decrypt(URLDecoder.decode(params, "utf-8").getBytes(StandardCharsets.UTF_8), SECRET_KEY);
                }
            } else {
                decryptvalue = URLDecoder.decode(decrypt(params.getBytes(StandardCharsets.UTF_8)), "utf-8");
            }
            decryptvalue = URLDecoder.decode(decryptvalue, "utf-8");
        } catch (Exception e) {
            log.error("decryptAllPara====>解密全部参数失败:", e);
            e.printStackTrace();
        }
        return decryptvalue;
    }

    /**
     * 解密
     *
     * @param src
     * @return
     * @throws Exception
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
     * @return
     * @throws Exception
     */
    public static String Decrypt(byte[] src, String key) throws Exception {
        SecretKey sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.DECRYPT_MODE, sKey);
        byte[] b = Base64Utils.decodeFromString(new String(src, StandardCharsets.UTF_8).trim());
        byte[] result = ci.doFinal(b);
        return new String(result, StandardCharsets.UTF_8);
    }


    /**
     * @param src
     * @throws Exception
     * @return加密
     */
    public static String Encrypt(byte[] src) throws Exception {
        SecretKey sKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] b = ci.doFinal(src);
        return Base64Utils.encodeToString(b);
    }


    /**
     * @param src
     * @throws Exception
     * @return加密
     */
    public static String Encrypt(byte[] src, String key) throws Exception {
        SecretKey sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] b = ci.doFinal(src);
        return Base64Utils.encodeToString(b);
    }


    public static void main(String[] args) {
        try {
            String param = "function=/api/payNotify/" +
                    "applePay&trade_no=1521433835928023040&totalAmount=0.01&total_amount=0.01&sign=MIIVPwYJKoZIhvcNAQcCoIIVMDCCFSwCAQExCzAJBgUrDgMCGgUAMIIE4AYJKoZIhvcNAQcBoIIE0QSCBM0xggTJMAoCAQgCAQEEAhYAMAoCARQCAQE" +
                    "EAgwAMAsCAQECAQEEAwIBADALAgEDAgEBBAMMATEwCwIBCwIBAQQDAgEAMAsCAQ8CAQEEAwIBADALAgEQAgEBBAMCAQAwCwIBGQIBAQQDAgEDMAwCAQoCAQEEBBYCNCswDAIBDgIBAQQEAgIAvTANAgENAgEBBAUCAwIkcDANAg" +
                    "ETAgEBBAUMAzEuMDAOAgEJAgEBBAYCBFAyNTYwGAIBBAIBAgQQUjblCuJKoP8FhOthpsTjhzAbAgEAAgEBBBMMEVByb2R1Y3Rpb25TYW5kYm94MBsCAQICAQEEEwwRY29tLnRlc3QudGVzdDIwMjAwHAIBBQIBAQQUlmJ+Z4AOHCFM2Za9Ig/" +
                    "be+Y2YCEwHgIBDAIBAQQWFhQyMDIyLTA1LTAzVDEwOjE4OjEzWjAeAgESAgEBBBYWFDIwMTMtMDgtMDFUMDc6MDA6MDBaMEgCAQcCAQEEQOyfQrrkL3MwJ2hBoJ6JNIVfhdv92d7+JxdL/fE8wb0yeYjCf4Hyau/IF1m/81eM2ljNll1Ylq/" +
                    "thCOMsYn3cEkwUQIBBgIBAQRJSGkzYCGwV+c1JDZ5IrI7BWTdKTuLMwV/" +
                    "OoUJJ+E3oWULijDdTV5iJ6wrOT5k3ikGUReeXEYcBSccOkOSq5co3+CWFcDYz4aPxDCCAWECARECAQEEggFXMYIBUzALAgIGrAIBAQQCFgAwCwICBq0CAQEEAgwAMAsCAgawAgEBBAIWADALAgIGsgIBAQQCDAAwCwICBrMCAQEEAgwAMAs" +
                    "CAga0AgEBBAIMADALAgIGtQIBAQQCDAAwCwICBrYCAQEEAgwAMAwCAgalAgEBBAMCAQEwDAICBqsCAQEEAwIBATAMAgIGrgIBAQQDAgEAMAwCAgavAgEBBAMCAQAwDAICBrECAQEEAwIBADAMAgIGugIBAQQDAgEAMBkCAgamAgEBBBA" +
                    "MDmZrZ2FtZTUuNl90ZXN0MBsCAganAgEBBBIMEDIwMDAwMDAwNDE5MzEwMTQwGwICBqkCAQEEEgwQMjAwMDAwMDA0MTkzMTAxNDAfAgIGqAIBAQQWFhQyMDIyLTA0LTI3VDA3OjI4OjAzWjAfAgIGqgIBAQQWFhQyMDIyLTA0LTI3VDA3OjI4Oj" +
                    "AzWjCCAWECARECAQEEggFXMYIBUzALAgIGrAIBAQQCFgAwCwICBq0CAQEEAgwAMAsCAgawAgEBBAIWADALAgIGsgIBAQQCDAAwCwICBrMCAQEEAgwAMAsCAga0AgEBBAIMADALAgIGtQIBAQQCDAAwCwICBrYCAQEEAgwAMAwCAgalAg" +
                    "EBBAMCAQEwDAICBqsCAQEEAwIBATAMAgIGrgIBAQQDAgEAMAwCAgavAgEBBAMCAQAwDAICBrECAQEEAwIBADAMAgIGugIBAQQDAgEAMBkCAgamAgEBBBAMDmZrZ2FtZTUuNl90ZXN0MBsCAganAgEBBBIMEDIwMDAwMDAwNDU4ODczN" +
                    "zcwGwICBqkCAQEEEgwQMjAwMDAwMDA0NTg4NzM3NzAfAgIGqAIBAQQWFhQyMDIyLTA1LTAzVDEwOjE4OjEzWjAfAgIGqgIBAQQWFhQyMDIyLTA1LTAzVDEwOjE4OjEzWqCCDmUwggV8MIIEZKADAgECAggO61eH554JjTANBgkqhkiG9w0BAQUF" +
                    "ADCBljELMAkGA1UEBhMCVVMxEzARBgNVBAoMCkFwcGxlIEluYy4xLDAqBgNVBAsMI0FwcGxlIFdvcmxkd2lkZSBEZXZlbG9wZXIgUmVsYXRpb25zMUQwQgYDVQQDDDtBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9ucyBDZXJ0aWZp" +
                    "Y2F0aW9uIEF1dGhvcml0eTAeFw0xNTExMTMwMjE1MDlaFw0yMzAyMDcyMTQ4NDdaMIGJMTcwNQYDVQQDDC5NYWMgQXBwIFN0b3JlIGFuZCBpVHVuZXMgU3RvcmUgUmVjZWlwdCBTaWduaW5nMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZ" +
                    "GUgRGV2ZWxvcGVyIFJlbGF0aW9uczETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQClz4H9JaKBW9aH7SPaMxyO4iPApcQmyz3Gn+xKDVWG/" +
                    "6QC15fKOVRtfX+yVBidxCxScY5ke4LOibpJ1gjltIhxzz9bRi7GxB24A6lYogQ+IXjV27fQjhKNg0xbKmg3k8LyvR7E0qEMSlhSqxLj7d0fmBWQNS3CzBLKjUiB91h4VGvojDE2H0oGDEdU8zeQuLKSiX1fpIVK4cCc4Lqku4KXY/Qrk8H9Pm/" +
                    "KwfU8qY9SGsAlCnYO3v6Z/v/Ca/VbXqxzUUkIVonMQ5DMjoEC0KCXtlyxoWlph5AQaCYmObgdEHOwCl3Fc9DfdjvYLdmIHuPsB8/ijtDT+iZVge/iA0kjAgMBAAGjggHXMIIB0zA/" +
                    "BggrBgEFBQcBAQQzMDEwLwYIKwYBBQUHMAGGI2h0dHA6Ly9vY3NwLmFwcGxlLmNvbS9vY3NwMDMtd3dkcjA0MB0GA1UdDgQWBBSRpJz8xHa3n6CK9E31jzZd7SsEhTAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFIgnFwmpthhgi+zruvZHWc" +
                    "VSVKO3MIIBHgYDVR0gBIIBFTCCAREwggENBgoqhkiG92NkBQYBMIH+MIHDBggrBgEFBQcCAjCBtgyBs1JlbGlhbmNlIG9uIHRoaXMgY2VydGlmaWNhdGUgYnkgYW55IHBhcnR5IGFzc3VtZXMgYWNjZXB0YW5jZSBvZiB0aGUgdGhlbiBhcHBsaWNhYm" +
                    "xlIHN0YW5kYXJkIHRlcm1zIGFuZCBjb25kaXRpb25zIG9mIHVzZSwgY2VydGlmaWNhdGUgcG9saWN5IGFuZCBjZXJ0aWZpY2F0aW9uIHByYWN0aWNlIHN0YXRlbWVudHMuMDYGCCsGAQUFBwIBFipodHRwOi8vd3d3LmFwcGxlLmNvbS9jZXJ0aWZp" +
                    "Y2F0ZWF1dGhvcml0eS8wDgYDVR0PAQH/BAQDAgeAMBAGCiqGSIb3Y2QGCwEEAgUAMA0GCSqGSIb3DQEBBQUAA4IBAQANphvTLj3jWysHbkKWbNPojEMwgl/gXNGNvr0PvRr8JZLbjIXDgFnf4+LXLgUUrA3btrj+/DUufMutF2uOfx/" +
                    "kd7mxZ5W0E16mGYZ2+FogledjjA9z/Ojtxh+umfhlSFyg4Cg6wBA3LbmgBDkfc7nIBf3y3n8aKipuKwH8oCBc2et9J6Yz+PWY4L5E27FMZ/xuCk/J4gao0pfzp45rUaJahHVl0RYEYuPBX/UIqc9o2ZIAycGMs/" +
                    "iNAGS6WGDAfK+PdcppuVsq1h1obphC9UynNxmbzDscehlD86Ntv0hgBgw2kivs3hi1EdotI9CO/" +
                    "KBpnBcbnoB7OUdFMGEvxxOoMIIEIjCCAwqgAwIBAgIIAd68xDltoBAwDQYJKoZIhvcNAQEFBQAwYjELMAkGA1UEBhMCVVMxEzARBgNVBAoTCkFwcGxlIEluYy4xJjAkBgNVBAsTHUFwcGxlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRYwFAYDVQQD" +
                    "Ew1BcHBsZSBSb290IENBMB4XDTEzMDIwNzIxNDg0N1oXDTIzMDIwNzIxNDg0N1owgZYxCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEI" +
                    "GA1UEAww7QXBwbGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDKOFSmy1aqyCQ5SOmM7uxfuH8mkbw0U3rOfGOAYXdkXqUHI7Y5/" +
                    "lAtFVZYcC1+xG7BSoU+L/DehBqhV8mvexj/avoVEkkVCBmsqtsqMu2WY2hSFT2Miuy/axiV4AOsAX2XBWfODoWVN2rtCbauZ81RZJ/" +
                    "GXNG8V25nNYB2NqSHgW44j9grFU57Jdhav06DwY3Sk9UacbVgnJ0zTlX5ElgMhrgWDcHld0WNUEi6Ky3klIXh6MSdxmilsKP8Z35wugJZS3dCkTm59c3hTO/" +
                    "AO0iMpuUhXf1qarunFjVg0uat80YpyejDi+l5wGphZxWy8P3laLxiX27Pmd3vG2P+kmWrAgMBAAGjgaYwgaMwHQYDVR0OBBYEFIgnFwmpthhgi+zruvZHWcVSVKO3MA8GA1UdEwEB/" +
                    "wQFMAMBAf8wHwYDVR0jBBgwFoAUK9BpR5R2Cf70a40uQKb3R01/CF4wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDovL2NybC5hcHBsZS5jb20vcm9vdC5jcmwwDgYDVR0PAQH/" +
                    "BAQDAgGGMBAGCiqGSIb3Y2QGAgEEAgUAMA0GCSqGSIb3DQEBBQUAA4IBAQBPz+9Zviz1smwvj+4ThzLoBTWobot9yWkMudkXvHcs1Gfi/ZptOllc34MBvbKuKmFysa/" +
                    "Nw0Uwj6ODDc4dR7Txk4qjdJukw5hyhzs+r0ULklS5MruQGFNrCk4QttkdUGwhgAqJTleMa1s8Pab93vcNIx0LSiaHP7qRkkykGRIZbVf1eliHe2iK5IaMSuviSRSqpd1VAKmuu0swruGgsbwpgOYJd+W+NKIByn/" +
                    "c4grmO7i77LpilfMFY0GCzQ87HUyVpNur+cmV6U/kTecmmYHpvPm0KdIBembhLoz2IYrF+Hjhga6/05Cdqa3zr/" +
                    "04GpZnMBxRpVzscYqCtGwPDBUfMIIEuzCCA6OgAwIBAgIBAjANBgkqhkiG9w0BAQUFADBiMQswCQYDVQQGEwJVUzETMBEGA1UEChMKQXBwbGUgSW5jLjEmMCQGA1UECxMdQXBwbGUgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkxFjAUBgNVB" +
                    "AMTDUFwcGxlIFJvb3QgQ0EwHhcNMDYwNDI1MjE0MDM2WhcNMzUwMjA5MjE0MDM2WjBiMQswCQYDVQQGEwJVUzETMBEGA1UEChMKQXBwbGUgSW5jLjEmMCQGA1UECxMdQXBwbGUgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkxFjAUBgNVB" +
                    "AMTDUFwcGxlIFJvb3QgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDkkakJH5HbHkdQ6wXtXnmELes2oldMVeyLGYne+Uts9QerIjAC6Bg+" +
                    "+FAJ039BqJj50cpmnCRrEdCju+QbKsMflZ56DKRHi1vUFjczy8QPTc4UadHJGXL1XQ7Vf1+b8iUDulWPTV0N8WQ1IxVLFVkds5T39pyez1C6wVhQZ48ItCD3y6wsIG9wtj8BMIy3Q88PnT3zK0koGsj+zrW5DtleHNbLPbU6rfQPDgCSC7EhFi501TwN22IWq6Nx" +
                    "kkdTVcGvL0Gz+PvjcM3mo0xFfh9Ma1CWQYnEdGILEINBhzOKgbEwWOxaBDKMaLOPHd5lc/9nXmW8Sdh2nzMUZaF3lMktAgMBAAGjggF6MIIBdjAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/" +
                    "zAdBgNVHQ4EFgQUK9BpR5R2Cf70a40uQKb3R01/CF4wHwYDVR0jBBgwFoAUK9BpR5R2Cf70a40uQKb3R01/" +
                    "CF4wggERBgNVHSAEggEIMIIBBDCCAQAGCSqGSIb3Y2QFATCB8jAqBggrBgEFBQcCARYeaHR0cHM6Ly93d3cuYXBwbGUuY29tL2FwcGxlY2EvMIHDBggrBgEFBQcCAjCBthqBs1JlbGlhbmNlIG9uIHRoaXMgY2VydGlmaWNhdGUgYnkgYW55IHBhcnR" +
                    "5IGFzc3VtZXMgYWNjZXB0YW5jZSBvZiB0aGUgdGhlbiBhcHBsaWNhYmxlIHN0YW5kYXJkIHRlcm1zIGFuZCBjb25kaXRpb25zIG9mIHVzZSwgY2VydGlmaWNhdGUgcG9saWN5IGFuZCBjZXJ0aWZpY2F0aW9uIHByYWN0aWNlIHN0YXRlbWVudHMuMA" +
                    "0GCSqGSIb3DQEBBQUAA4IBAQBcNplMLXi37Yyb3PN3m/J20ncwT8EfhYOFG5k9RzfyqZtAjizUsZAS2L70c5vu0mQPy3lPNNiiPvl4/2vIB+x9OYOLUyDTOMSxv5pPCmv/K/" +
                    "xZpwUJfBdAVhEedNO3iyM7R6PVbyTi69G3cN8PReEnyvFteO3ntRcXqNx+IjXKJdXZD9Zr1KIkIxH3oayPc4FgxhtbCS+SsvhESPBgOJ4V9T0mZyCKM2r3DYLP3uujL/lTaltkwGMzd/c6ByxW69oPIQ7aunMZT7XZNn/" +
                    "Bh1XZp5m5MkL72NVxnn6hUrcbvZNCJBIqxw8dtk2cXmPIS4AXUKqK1drk/" +
                    "NAJBzewdXUhMYIByzCCAccCAQEwgaMwgZYxCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3aWRlIE" +
                    "RldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkCCA7rV4fnngmNMAkGBSsOAwIaBQAwDQYJKoZIhvcNAQEBBQAEggEAP/" +
                    "Bj88vGlZXjhvePurpdZfGuQCVjB0QhKNL6TXpQQ9X2QzO81kswUiMDQVggVWspUuDQhiZPoYxU2QzUFN58wLmKL1d+31thHJpWhAvIDsQ+Zyg5gqCHjSV7E3KRoxFJyHbD2yh6EpkB2hpd2dc9q8c5HGCEnVo0nCjvm4gxip9xNZnz585snlOp+qRFQ0fwy" +
                    "br0OfzM9iUhohPBeUanPsI+hFvcJgWMTBVSFSukYAK7tY06OLaCcjaXIlY1wu2w/6+T3GP5zaaMJc4gOxq/YF6rDISHX/8eKvglHscRF0NI1lqao0mPMUbCExSjSs5nxLpNFC+7pOv20frbNamTzg==&userId=1520320807190917120";
            System.out.println("请求加密param:" + param);
            String secret = URLEncoder.encode(param, "utf-8");
            System.out.println("URLenCode:" + secret);
            String str = SecurityUtil.Encrypt(secret.getBytes(StandardCharsets.UTF_8), "SfNJN1O69Zs1ekjB");
            System.out.println("ios加密后str:" + str);
            String encode = URLEncoder.encode(str, "utf-8");
            System.out.println("(这个要传给服务器了)---加密后再Url.enCode:" + encode);

            System.out.println("------------------------------------------------------");
            String accessSecretData = URLDecoder.decode(encode, "utf-8");
            String result = SecurityUtil.Decrypt(accessSecretData.getBytes(StandardCharsets.UTF_8), "SfNJN1O69Zs1ekjB");
            result = URLDecoder.decode(result, "utf-8");
            System.out.println("服务器解密后str:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}