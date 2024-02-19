package com.nova.smg.sms.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.nova.smg.sms.config.SmsProperties;
import com.nova.smg.sms.entity.SmsData;
import com.nova.smg.sms.entity.SmsResponse;
import com.nova.smg.sms.service.SmsTemplate;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * @description: 七牛云短信
 * @author: wzh
 * @date: 2023/4/20 20:03
 */
@Slf4j(topic = "QiNiuSmsTemplate")
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
            log.error("异常信息:", e);
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }


}
