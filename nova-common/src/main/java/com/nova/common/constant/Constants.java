package com.nova.common.constant;

/**
 * @description: 通用常量信息
 * @author: wzh
 * @date: 2022/8/4 21:29
 */
public class Constants {

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";


    public static final String ZERO = "0";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    public static final String YEE_CODE = "OPR00000";

    public static final int KS_CODE = 1;

    public static final boolean IS_OPEN = true;

    public static final String CPAPI_URL = "http://cpapi.fengkuangtiyu.cn/api/mobileClientApi.action";

    public static final String CPAPI_URL_DEV = "http://cpapics.fengkuangtiyu.cn/api/mobileClientApi.action";

    public static final String GOOGLE_VERIFY_URL = "http://gpay.fengkuangtiyu.cn/api/google/verify";

    /**
     * 加款接口url,调用用户加款—web（AccountAction.chargeBack）--ChargeBackAfterService.after--chargeToHd
     */
    public static final String PAY_URL = "http://goucai.nb.com/uc/account/chargeback.action";

    /**
     * 充值调用web接口的时候验证md5的key
     */
    public static final String MD5_KEY = "ekcamjlfwjnc6s9vir1";

    public static final String REDIS_KEY = "fk_pay_center_";

    public static final String DESTINATION_NAME_RECHARGE = "jms.add.fee.url.queue";

    /**
     * 清算通知地址
     * 该回调地址通过下单接口的csUrl参数上送，接到清算回调的订单才可以进行分账（实时分账也可以将清算回调处理的进程作为分账触发）
     */
    public static final String CS_NOTIFY_URL = "http://zhongcangpay.solisoli.top/api/payNotify/yeePay/divideApply";

    /**
     * 开通钱包通知地址
     * http://zhongcangpay.solisoli.top/api/userSetting/yeePay/notify
     */
    public static final String WALLET_NOTIFY_URL = "http://zhongcangpay.solisoli.top/api/userSetting/yeePay/wallet/notify";

    /**
     * 商户入网通知地址-小微(个人)
     * 入网结果通知:https://open.yeepay.com/docs/products/ptssfk/spis/5f3de63c20289f001ba82527
     */
    public static final String MICRO_NOTIFY_URL = "http://zhongcangpay.solisoli.top/api/userSetting/yeePay/account/notify";
}
