package com.nova.pay.entity.param;

import lombok.Data;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/8/23 21:19
 */
@Data
public class WeChatMpParam extends BaseParam {

    private static final long serialVersionUID = -2421027499162168963L;

    /**
     * 支付配置id
     */
    private Long payConfigId;

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 微信用户openId
     */
    private String openId;

    /**
     * 语言类型
     * zh_CN, zh_TW, en
     */
    private String lang = "zh_CN";


}
