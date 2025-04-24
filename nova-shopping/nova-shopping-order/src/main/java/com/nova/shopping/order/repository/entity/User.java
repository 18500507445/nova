package com.nova.shopping.order.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(MyUser)实体类
 *
 * @author: wzh
 * @since 2023-04-15 15:53:19
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = -45219810911120823L;

    public static final String USER_TOTAL_REQUEST = "user_total_request";

    public static final String USER = "user";

    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户状态 0正常，1封号
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

