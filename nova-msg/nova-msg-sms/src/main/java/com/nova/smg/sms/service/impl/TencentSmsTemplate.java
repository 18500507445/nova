package com.nova.smg.sms.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.smg.sms.config.SmsProperties;
import com.nova.smg.sms.entity.SmsData;
import com.nova.smg.sms.entity.SmsResponse;
import com.nova.smg.sms.service.SmsTemplate;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Collection;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/20 20:22
 */
@RequiredArgsConstructor
public class TencentSmsTemplate implements SmsTemplate {

    private final SmsProperties smsProperties;

    private final Credential credential;

    @Override
    @SneakyThrows
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        //支持的地域列表参考 https://cloud.tencent.com/document/api/382/52071#.E5.9C.B0.E5.9F.9F.E5.88.97.E8.A1.A8 */
        SmsClient client = new SmsClient(credential, "ap-beijing");
        //实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
        SendSmsRequest req = new SendSmsRequest();
        req.setSmsSdkAppId(smsProperties.getAppId());
        req.setSignName(smsProperties.getSignName());
        req.setTemplateId(smsProperties.getTemplateId());
        req.setPhoneNumberSet(ArrayUtil.toArray(phones, String.class));
        SendSmsResponse res = client.SendSms(req);
        SendStatus sendStatus = res.getSendStatusSet()[0];
        return new SmsResponse(StrUtil.equals("ok", sendStatus.getCode()), Convert.toInt(sendStatus.getCode()), sendStatus.getMessage());
    }
}
