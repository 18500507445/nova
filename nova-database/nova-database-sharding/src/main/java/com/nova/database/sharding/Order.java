package com.nova.database.sharding;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单实体类
 *
 * @author Jiahai
 */
@Accessors(chain = true)
@TableName("t_order")
@Data
public class Order implements Serializable {

    /**
     * 订单ID
     */
    private Integer orderId;

    //用户姓名
    private String userName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}