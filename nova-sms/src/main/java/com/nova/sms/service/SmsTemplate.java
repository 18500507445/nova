package com.nova.sms.service;

import com.nova.sms.entity.SmsData;
import com.nova.sms.entity.SmsInfo;
import com.nova.sms.entity.SmsResponse;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * @description: 短信发送封装接口
 * @author: wzh
 * @date: 2023/4/20 19:52
 */
public interface SmsTemplate {

    /**
     * 发送短信
     *
     * @param smsInfo 短信信息
     * @return 发送返回
     */
    default boolean send(SmsInfo smsInfo) {
        return sendMulti(smsInfo.getSmsData(), smsInfo.getPhones());
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phone   手机号
     * @return 发送返回
     */
    default boolean sendSingle(SmsData smsData, String phone) {
        if (StringUtils.isEmpty(phone)) {
            return Boolean.FALSE;
        }
        return sendMulti(smsData, Collections.singletonList(phone));
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phones  手机号列表
     * @return 发送返回
     */
    default boolean sendMulti(SmsData smsData, Collection<String> phones) {
        SmsResponse response = sendMessage(smsData, phones);
        return response.isSuccess();
    }

    /**
     * 发送短信
     *
     * @param smsData 短信内容
     * @param phones  手机号列表
     * @return 发送返回
     */
    SmsResponse sendMessage(SmsData smsData, Collection<String> phones);
}
