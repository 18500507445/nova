package com.nova.pay.config;

/**
 * @Description: 微信配置类
 * @Author: wangzehui
 * @Date: 2022/6/18 20:36
 */
public class WechatPayConfig {

    /**
     * h5、app 支付的appId
     */
    public static final String APP_ID = "";

    /**
     * 应用秘钥
     */
    public static final String APP_SECRET = "";

    /**
     * 原生(微信内拉起支付)、小程序支付的appId
     */
    public static final String APP_ID_JSAPI = "";

    public static final String APP_SECRET_JSAPI = "";

    /**
     * 商户号 mchId
     */
    public static final String MCH_ID = "";

    /**
     * 支付秘钥
     */
    public static final String PAY_SECRET = "";


    /**
     * P12证书路径（退款需要用，线上是服务器路径
     */
    public static final String CERT_PATH = "/app/cert/1625094210/apiclient_cert.p12";

    /**
     * 支付类型，JSAPI(小程序也用这个)、APP(客户端)、NATIVE(扫码)、MWEB(h5)
     */
    public static final String TRADE_TYPE = "JSAPI";

    /**
     * 回掉地址
     */
    public static final String NOTIFY_URL = "";

    public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    public static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    /**
     * 返回状态成功
     */
    public static final String SUCCESS = "SUCCESS";

    public static final String RETURN_CODE = "return_code";

    public static final String RESULT_CODE = "result_code";


    /**
     * 回调处理后response响应通知微信失败xml
     */
    public static final String XML_FAIL = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";

    /**
     * 回调处理后response响应通知微信成功xml
     */
    public static final String XML_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**
     * 获取当前时间戳，单位秒
     *
     * @return
     */
    public static String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


}
