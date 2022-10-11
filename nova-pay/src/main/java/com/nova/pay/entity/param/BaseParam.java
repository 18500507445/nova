package com.nova.pay.entity.param;

import cn.hutool.core.util.StrUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.common.utils.security.UserNameSecretUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 公共请求参数
 * @Author: wangzehui
 * @Date: 2022/8/23 13:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseParam extends BaseController implements Serializable {

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
     * 客户类型:donggeqiu(金币充值)，fengkuangTY(金豆充值)
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

    public String getUserName() {
        if (StrUtil.isNotBlank(userName)) {
            userName = UserNameSecretUtil.userNameDecrypt(userName);
        }
        return userName;
    }

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
