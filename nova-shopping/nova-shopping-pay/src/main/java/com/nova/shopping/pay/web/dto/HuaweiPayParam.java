package com.nova.shopping.pay.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/14 19:17
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HuaweiPayParam {

    /**
     * OAuth 2.0客户端ID（凭据）
     */
    private String clientId;

    /**
     * 在AppGallery Connect创建应用之后，系统自动分配的公钥
     */
    private String clientSecret;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 购买凭证token
     */
    private String purchaseToken;

    /**
     * 华为接口调用凭证
     */
    private String accessToken;

}
