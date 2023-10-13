package com.nova.shopping.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表(MyGoods)实体类
 *
 * @author wzh
 * @since 2023-04-15 15:53:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MyGoods implements Serializable {

    private static final long serialVersionUID = 693632569075853902L;

    public static final String TOTAL_COUNT = "total_count";

    public static final String GOODS = "goods";

    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 售卖状态：0正常，1停售
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}

