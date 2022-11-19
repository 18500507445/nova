package com.nova.pay.entity.param;

import lombok.*;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/8/23 19:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam extends BaseParam {

    private static final long serialVersionUID = 5581334361189071295L;

    /**
     * 金额 单位元
     */
    private String totalAmount;

    /**
     * 1支付宝 2微信 3苹果 4易宝 5谷歌 6快手 99金币兑换
     */
    private Integer payWay;

    /**
     * 支付配置id
     */
    private Long payConfigId;
}
