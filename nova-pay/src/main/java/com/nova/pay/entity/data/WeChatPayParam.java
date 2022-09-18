package com.nova.pay.entity.data;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @Description: 微信支付请求类
 * @Author: wangzehui
 * @Date: 2022/8/4 21:41
 */
public class WeChatPayParam {

    public static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 金额
     */
    private String totalAmount;

    /**
     * 终端IP
     */
    private String createIp;

    /**
     * 交易类型 JSAPI(原始微信内支付、小程序也用这个)、APP(客户端)、NATIVE(扫码)、MWEB(h5)
     */
    private String tradeType;

    /**
     * authCode
     */
    private String authCode;

    /**
     * openId
     */
    private String openId;

    private String mchId;

    private String paySecret;

    private String certPath;

    public WeChatPayParam() {

    }

    public WeChatPayParam(String body, String outTradeNo, String totalAmount, String tradeType, String authCode, String createIp, String openId) {
        this.body = body;
        this.outTradeNo = outTradeNo;
        this.totalAmount = totalAmount;
        this.tradeType = tradeType;
        this.authCode = authCode;
        this.createIp = createIp;
        this.openId = openId;
    }

    public String getCreateIp() {
        String result = "";
        if (StringUtils.isNotBlank(createIp)) {
            String[] split = createIp.split(",");
            if (split.length > 1) {
                if (StringUtils.isNotBlank(split[1])) {
                    result = split[1];
                }
            } else {
                result = split[0];
            }
        }
        return result;
    }

    /**
     * 微信 金额 单位：分
     *
     * @return
     */
    public String getTotalAmount() {
        return checkZero(new BigDecimal(totalAmount).multiply(new BigDecimal("100")));
    }

    /**
     * 和0做校验并且末尾舍弃多余0
     *
     * @param price
     * @return
     */
    private String checkZero(BigDecimal price) {
        if (price.compareTo(ZERO) <= 0) {
            price = ZERO;
        }
        return price.stripTrailingZeros().toPlainString();
    }
}
