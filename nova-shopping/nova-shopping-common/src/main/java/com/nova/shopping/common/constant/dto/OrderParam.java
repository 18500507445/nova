package com.nova.shopping.common.constant.dto;

import lombok.*;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/14 19:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam extends PayReqDTO {

    private static final long serialVersionUID = 5581334361189071295L;

    /**
     * 金额 单位元
     */
    private String totalAmount;

    /**
     * 1支付宝 2微信 3苹果 4易宝 5谷歌 6快手
     */
    private Integer payWay;

    /**
     * 支付配置id
     */
    private Long payConfigId;

    /**
     * 应用程序包名
     */
    private String packageName;

    /**
     * 应用名称(ScorePredict1x2)
     */
    private String applicationName;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 谷歌返回的收据
     */
    private String purchaseToken;

    /**
     * 证书json文件地址
     */
    private String keyPath;
}
