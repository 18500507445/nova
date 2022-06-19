package config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;

/**
 * @Description: 支付宝配置类
 * @Author: wangzehui
 * @Date: 2022/3/17 13:10
 */
public class AliPayConfig {

    /**
     * h5、app 支付的appId
     */
    public static final String APP_ID = "";

    /**
     * 支付宝公钥
     */
    public static final String PUBLIC_KEY = "";

    /**
     * 应用私钥
     */
    public static final String PRIVATE_KEY = "";

    /**
     * 小程序appId
     */
    public static final String APPLET_APP_ID = "";

    /**
     * 小程序 支付宝公钥
     */
    public static final String APPLET_PUBLIC_KEY = "";

    /**
     * 小程序 应用私钥
     */
    public static final String APPLET_PRIVATE_KEY = "";

    public static final String CERT_PATH = "";

    public static final String PUBLIC_CERT_PATH = "";

    public static final String ROOT_CERT_PATH = "";

    /**
     * 支付宝网关地址： 沙箱环境用：https://openapi.alipaydev.com/gateway.do
     */
    public static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    public static final String SIGN_TYPE = "RSA2";

    public static final String FORMAT = "json";

    public static final String CHARSET = "UTF-8";

    /**
     * 支付通知Url
     */
    public static final String NOTIFY_URL = "";

    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final String NOTIFY_SUCCESS = "success";

    public static final String NOTIFY_FAIL = "fail";

    public static final String REDIS_KEY = "AliPayConfig";

    /**
     * 备注:现在不用
     * 证书连接方式
     * 提交数据至支付宝时请使用 alipayClient.certificateExecute(request);
     *
     * @return
     */
    public AlipayClient getAlipayCertClient() throws AlipayApiException {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(GATEWAY_URL);
        certAlipayRequest.setAppId(APP_ID);
        certAlipayRequest.setPrivateKey(PRIVATE_KEY);
        certAlipayRequest.setFormat(FORMAT);
        certAlipayRequest.setCharset(CHARSET);
        certAlipayRequest.setSignType(SIGN_TYPE);
        certAlipayRequest.setCertPath(CERT_PATH);
        certAlipayRequest.setAlipayPublicCertPath(PUBLIC_CERT_PATH);
        certAlipayRequest.setRootCertPath(ROOT_CERT_PATH);
        return new DefaultAlipayClient(certAlipayRequest);
    }

    /**
     * 小程序连接方式
     *
     * @return
     */
    public static AlipayClient getAppleClient() {
        return new DefaultAlipayClient(GATEWAY_URL, APPLET_APP_ID, APPLET_PRIVATE_KEY, FORMAT, CHARSET, APPLET_PUBLIC_KEY, SIGN_TYPE);
    }

    /**
     * h5、app连接方式
     *
     * @return
     */
    public static AlipayClient getDefaultClient() {
        return new DefaultAlipayClient(GATEWAY_URL, APP_ID, PRIVATE_KEY, FORMAT, CHARSET, PUBLIC_KEY, SIGN_TYPE);
    }

}
