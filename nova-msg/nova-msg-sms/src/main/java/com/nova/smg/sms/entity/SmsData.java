package com.nova.smg.sms.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @description: 通知内容
 * @author: wzh
 * @date: 2023/4/20 19:54
 */
@Data
@Accessors(chain = true)
public class SmsData implements Serializable {

    private static final long serialVersionUID = 335828687656517750L;

    /**
     * 构造器
     *
     * @param params 参数列表
     */
    public SmsData(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 变量key,用于从参数列表获取变量值
     */
    private String key;

    /**
     * 参数列表
     */
    private Map<String, String> params;
}
