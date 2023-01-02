package com.nova.common.core.model.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据传输对象（Data Transfer Object）用于展示层和逻辑层之间的数据传输。
 * @description: 基本的请求DTO
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
@Data
public class BaseReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新人姓名
     */
    private String updateUserName;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 创建人IP
     */
    private String createIp;

    /**
     * 更新人IP
     */
    private String updateIp;

    /**
     * 删除标志-1: 已删除 0：未删除
     */
    private Integer deleteFlag;

    /**
     * 创建人姓名
     */
    private String createUserName;
}
