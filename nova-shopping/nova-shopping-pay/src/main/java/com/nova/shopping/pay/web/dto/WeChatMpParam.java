package com.nova.shopping.pay.web.dto;

import com.nova.shopping.common.constant.dto.PayReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 微信公众号
 * @author: wzh
 * @date: 2023/4/14 19:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeChatMpParam extends PayReqDTO {

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
