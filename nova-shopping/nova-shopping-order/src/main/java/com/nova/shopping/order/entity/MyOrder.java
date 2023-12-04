package com.nova.shopping.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表(MyOrder)实体类
 *
 * @author wzh
 * @since 2023-04-15 15:53:19
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MyOrder implements Serializable {

    private static final long serialVersionUID = -92127017631864976L;


    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 订单状态：0默认，1成功，2已发货，3失败，4退款，5过期
     */
    private Integer status;

    /**
     * 支付状态：0默认，1成功，2处理中，3失败，4退款
     */
    private Integer payStatus;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}

