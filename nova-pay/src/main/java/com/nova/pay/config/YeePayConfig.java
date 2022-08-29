package com.nova.pay.config;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/7/1 10:20
 */
public class YeePayConfig {

    /**
     * app_key:app_10088528644
     */
    public static final String APP_KEY = "";

    /**
     * 易宝公钥
     */
    public static final String PUBLIC_KEY = "";

    /**
     * 商户私钥
     */
    public static final String PRIVATE_KEY = "";

    /**
     * 发起方商编
     * 发起方商户编号（标准商户收付款方案中此参数与商编一致，平台商户收付款方案中此参数为平台商商户编号）
     */
    public static final String PARENT_MERCHANT_NO = "10085537650";

    /**
     * 商户编号:易宝支付分配的的商户唯一标识
     */
    public static final String MERCHANT_NO = "10085537650";

    /**
     * 支付通知地址
     */
    public static final String PAY_NOTIFY_URL = "http://xxx/api/payNotify/yeePay";

    /**
     * 清算通知地址
     * 该回调地址通过下单接口的csUrl参数上送，接到清算回调的订单才可以进行分账（实时分账也可以将清算回调处理的进程作为分账触发）
     */
    public static final String CS_NOTIFY_URL = "http://xxx/api/payNotify/yeePay/divideApply";

    /**
     * 开通钱包通知地址
     * http://xxx/api/userSetting/yeePay/notify
     */
    public static final String WALLET_NOTIFY_URL = "http://xxx/api/userSetting/yeePay/wallet/notify";

    /**
     * 商户入网通知地址-小微(个人)
     * 入网结果通知:https://open.yeepay.com/docs/products/ptssfk/spis/5f3de63c20289f001ba82527
     */
    public static final String MICRO_NOTIFY_URL = "http://xxx/api/userSetting/yeePay/account/notify";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

}
