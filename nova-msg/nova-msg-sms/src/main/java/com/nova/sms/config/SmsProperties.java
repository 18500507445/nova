package com.nova.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/20 19:50
 */
@Data
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {

    public static final String PREFIX = "sms";

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 短信服务名称
     */
    private String name;

    /**
     * appId
     */
    private String appId;

    /**
     * 短信模板ID
     */
    private String templateId;

    /**
     * regionId
     */
    private String regionId = "cn-hangzhou";

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretId
     */
    private String secretId;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 短信签名
     */
    private String signName;
}
