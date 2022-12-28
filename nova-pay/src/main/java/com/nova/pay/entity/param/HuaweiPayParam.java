package com.nova.pay.entity.param;

import lombok.*;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/12/8 10:05
 */
@Data
@Builder
@ToString
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
