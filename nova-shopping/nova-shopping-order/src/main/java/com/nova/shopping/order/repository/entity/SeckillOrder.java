package com.nova.shopping.order.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀订单表(MySeckillOrder)实体类
 *
 * @author: wzh
 * @since 2023-04-15 15:53:19
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 489814805628794925L;

    public static final String SECKILL_ORDER = "seckill_order";

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
     * 订单id
     */
    private Long orderId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}

