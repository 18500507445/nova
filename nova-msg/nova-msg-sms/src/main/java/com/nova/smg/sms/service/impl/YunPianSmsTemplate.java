package com.nova.smg.sms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nova.smg.sms.config.SmsProperties;
import com.nova.smg.sms.entity.SmsData;
import com.nova.smg.sms.entity.SmsResponse;
import com.nova.smg.sms.service.SmsTemplate;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.constant.Code;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsBatchSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

/**
 * @description: 云片短信
 * @author: wzh
 * @date: 2023/4/20 20:17
 */
@Slf4j(topic = "YunPianSmsTemplate")
@RequiredArgsConstructor
public class YunPianSmsTemplate implements SmsTemplate {

    private final SmsProperties smsProperties;

    private final YunpianClient client;

    public static final String HASH = "#";

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        String templateId = smsProperties.getTemplateId();
        // 云片短信模板内容替换, 占位符格式为官方默认的 #code#
        if (CollUtil.isNotEmpty(smsData.getParams())) {
            String templateText = JSONUtil.toJsonStr(smsData.getParams()).replace(HASH + "code" + HASH, templateId);
            Map<String, String> param = client.newParam(2);
            param.put(YunpianClient.MOBILE, StrUtil.join(",", phones));
            param.put(YunpianClient.TEXT, templateText);
            Result<SmsBatchSend> result = client.sms().multi_send(param);
            return new SmsResponse(result.getCode() == Code.OK, result.getCode(), result.toString());
        }
        return null;
    }
}
