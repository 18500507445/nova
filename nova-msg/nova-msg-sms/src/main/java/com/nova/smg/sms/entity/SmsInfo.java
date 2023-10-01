package com.nova.smg.sms.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * @description: 通知信息
 * @author: wzh
 * @date: 2023/4/20 19:54
 */
@Data
@Accessors(chain = true)
public class SmsInfo implements Serializable {

    private static final long serialVersionUID = 4728418713768808028L;

    /**
     * 通知内容
     */
    private SmsData smsData;

    /**
     * 号码列表
     */
    private Collection<String> phones;
}

