package com.nova.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wzh
 * @description: 阿里oss配置类
 * @date: 2024/05/08 14:38
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOssConfig {

    //Access Key ID
    private String accessKeyId;

    //Access Key Secret
    private String accessKeySecret;

    //桶名称
    private String bucketName;

    //访问域名
    private String endpoint;
}
