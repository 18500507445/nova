package com.nova.orm.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author: wzh
 * @description: order表
 * @date: 2023/06/21 17:03
 */
@Data
@Accessors(chain = true)
@TableName(value = "`order`")
public class Order {

    /**
     * 主键ID
     */
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
}
