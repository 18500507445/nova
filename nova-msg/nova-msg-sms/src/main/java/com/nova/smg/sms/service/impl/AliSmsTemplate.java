package com.nova.smg.sms.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.nova.smg.sms.config.SmsProperties;
import com.nova.smg.sms.entity.SmsData;
import com.nova.smg.sms.entity.SmsResponse;
import com.nova.smg.sms.service.SmsTemplate;
import com.yunpian.sdk.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Map;

/**
 * @description: 阿里云短信
 * @author: wzh
 * @date: 2023/4/20 19:57
 */
@Slf4j(topic = "AliSmsTemplate")
@RequiredArgsConstructor
public class AliSmsTemplate implements SmsTemplate {

    private static final int SUCCESS = 200;
    private static final String FAIL = "fail";
    private static final String OK = "ok";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String VERSION = "2023-04-29";
    private static final String ACTION = "SendSms";

    private final SmsProperties smsProperties;

    private final IAcsClient acsClient;

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.setSysAction(ACTION);
        request.putQueryParameter("PhoneNumbers", StrUtil.join(",", phones));
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateId());
        request.putQueryParameter("TemplateParam", JsonUtil.toJson(smsData.getParams()));
        request.putQueryParameter("SignName", smsProperties.getSignName());
        try {
            CommonResponse response = acsClient.getCommonResponse(request);
            Map<String, Object> data = JSONUtil.parseObj(response.getData()).toBean(new TypeReference<Map<String, Object>>() {
            });
            String code = FAIL;
            if (data != null) {
                code = String.valueOf(data.get("Code"));
            }
            return new SmsResponse(response.getHttpStatus() == SUCCESS && code.equalsIgnoreCase(OK), response.getHttpStatus(), response.getData());
        } catch (ClientException e) {
            log.error("异常信息:", e);
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
