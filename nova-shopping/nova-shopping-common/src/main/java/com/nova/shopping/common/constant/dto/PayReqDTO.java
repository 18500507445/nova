package com.nova.shopping.common.constant.dto;

import com.nova.shopping.common.constant.BaseController;
import com.nova.shopping.common.enums.BusinessEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description: 公共请求参数
 * @author: wzh
 * @date: 2023/4/14 19:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PayReqDTO extends BaseController implements Serializable {

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
    private String version;

    /**
     * 业务code 默认1充值
     * {@link BusinessEnum}
     */
    private int businessCode = 1;

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

    /**
     * 请求ip
     * 微信支付注意：APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     */
    private String requestIp = getIp();

    public String getSource() {
        return getValue("source");
    }

    public String getSid() {
        return getValue("sid");
    }

    public String getClientType() {
        return getValue("clientType");
    }

    public String getNewVersion() {
        return getValue("newVersion");
    }
}
