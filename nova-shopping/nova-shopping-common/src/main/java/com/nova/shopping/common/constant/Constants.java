package com.nova.shopping.common.constant;

/**
 * @description: 常量类
 * @author: wzh
 * @date: 2023/4/14 18:57
 */
public class Constants {

    public static final String ZERO = "0";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final String YEE_CODE = "OPR00000";

    public static final int KS_CODE = 1;

    public static final String OK = "ok";

    public static final String REDIS_KEY = "my-mall:";

    public static final String GOOGLE_VERIFY_URL = "http://xxx.xxx.xxx/api/google/verify";

    /**
     * 清算通知地址
     * 该回调地址通过下单接口的csUrl参数上送，接到清算回调的订单才可以进行分账（实时分账也可以将清算回调处理的进程作为分账触发）
     */
    public static final String CS_NOTIFY_URL = "http://xxx.xxx.xxx/api/payNotify/yeePay/divideApply";

    /**
     * 开通钱包通知地址
     */
    public static final String WALLET_NOTIFY_URL = "http://xxx.xxx.xxx/api/userSetting/yeePay/wallet/notify";

    /**
     * 商户入网通知地址-小微(个人)
     * :<a href="https://open.yeepay.com/docs/products/ptssfk/spis/5f3de63c20289f001ba82527">入网结果通知</a>
     */
    public static final String MICRO_NOTIFY_URL = "http://xxx.xxx.xxx/api/userSetting/yeePay/account/notify";

    /**
     * 直连交换机(自带)
     */
    public static final String EXCHANGE_DIRECT = "amq.direct";

    /**
     * 死信交换机
     */
    public static final String EXCHANGE_DIRECT_DLX = "dlx.direct";

    /**
     * 死信队列
     */
    public static final String QUEUE_DIRECT_DLX = "queue-direct-dlx";

    /**
     * 异步下单队列
     */
    public static final String QUEUE_DIRECT_CREATE_ORDER = "queue-direct-createOrder";

    /**
     * 查询订单队列
     */
    public static final String QUEUE_DIRECT_QUERY_ORDER = "queue-direct-queryOrder";


}
