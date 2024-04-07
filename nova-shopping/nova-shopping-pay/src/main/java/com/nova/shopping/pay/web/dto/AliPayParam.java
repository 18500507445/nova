package com.nova.shopping.pay.web.dto;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @description: 支付宝支付实体类
 * @author: wzh
 * @date: 2023/4/14 19:17
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AliPayParam implements Serializable {

    private static final long serialVersionUID = 6177572682414811592L;

    private String appId;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 支付宝应用私钥
     */
    private String privateKey;

    /**
     * 商品的标题
     */
    private String subject;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 金额
     */
    private String totalAmount;

    /**
     * 对一笔交易的具体描述信息
     */
    private String body;

    /**
     * 支付成功后回跳地址,H5支付特有
     */
    @Builder.Default
    private String returnUrl = "http://backtoclient.com";

    /**
     * 通知url
     */
    private String notifyUrl;

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 支付宝小程序用户id
     */
    private String userId;

    /**
     * 原因
     */
    private String reason;


}

/**
 * 初始的类，废弃了
 */
@Deprecated
class AliPayConfig {

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
