package com.nova.pay.entity.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 公共请求参数
 * @Author: wangzehui
 * @Date: 2022/8/23 13:57
 */
@Data
public class BaseParam implements Serializable {

    private static final long serialVersionUID = -5289042149894135473L;

    /**
     * source
     */
    private String source;

    /**
     * 渠道号
     */
    private String sid;

    /**
     * 客户类型
     */
    private String clientType;

    /**
     * 版本号
     */
    private String newVersion;


    private String fromSource;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 20;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 内部userName
     */
    private String userName;

}
