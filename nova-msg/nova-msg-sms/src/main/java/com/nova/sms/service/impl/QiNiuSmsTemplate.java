package com.nova.sms.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.nova.sms.config.SmsProperties;
import com.nova.sms.entity.SmsData;
import com.nova.sms.entity.SmsResponse;
import com.nova.sms.service.SmsTemplate;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * @description: 七牛云短信
 * @author: wzh
 * @date: 2023/4/20 20:03
 */
@RequiredArgsConstructor
public class QiNiuSmsTemplate implements SmsTemplate {

    private final SmsProperties smsProperties;

    private final SmsManager smsManager;

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        try {
            Response response = smsManager.sendMessage(smsProperties.getTemplateId(), ArrayUtil.toArray(phones, String.class), smsData.getParams());
            return new SmsResponse(response.isOK(), response.statusCode, response.toString());
        } catch (QiniuException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }


}
