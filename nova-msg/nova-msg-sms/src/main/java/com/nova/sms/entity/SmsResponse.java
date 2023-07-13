package com.nova.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/20 19:54
 */
@Data
@AllArgsConstructor
public class SmsResponse implements Serializable {

    private static final long serialVersionUID = -8243013406812841125L;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String msg;
}
