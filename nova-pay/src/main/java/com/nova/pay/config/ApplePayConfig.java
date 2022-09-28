package com.nova.pay.config;

/**
 * @Description: 苹果支付配置类
 * @Author: wangzehui
 * @Date: 2022/6/18 13:36
 */
@Deprecated
public class ApplePayConfig {

    public static final String ZERO = "0";

    /**
     * 沙盒地址
     */
    public static final String BOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

    /**
     * 正式地址
     */
    public static final String PRO_URL = "https://buy.itunes.apple.com/verifyReceipt";

    /**
     * 回掉地址
     */
    public static final String NOTIFY_URL = "";

    /**
     * 退款通知地址
     */
    public static final String REFUND_NOTIFY_URL = "";


}
