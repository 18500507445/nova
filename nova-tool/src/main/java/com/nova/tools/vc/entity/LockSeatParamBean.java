package com.nova.tools.vc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 锁定座位请求参数
 * @author: wangzehui
 * @date: 2019/5/27 9:18
 */
@Data
public class LockSeatParamBean implements Serializable {
    private static final long serialVersionUID = -140777507179975803L;

    /**
     * 系统商类型
     */
    private Integer cType;

    /**
     * 座位ID（用英文'|'分割）
     */
    public String seatIds;

    /**
     * 座位信息 （用英文'|'分割）
     */
    public String seatNames;

    /**
     * 排期Id
     */
    public String planId;

    /**
     * 用户手机号
     */
    public String mobile;


}
