package com.nova.starter.xxl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/22 22:10
 */
// 指定了前缀为"xxl.job"的配置属性，可以再其它类里进行注入使用了
@ConfigurationProperties("xxl.job")
@Validated
@Data
public class XxlProperties {

    /**
     * 是否开启，默认为 true 关闭
     */
    private Boolean enabled = true;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 控制器配置
     */
    @NotNull(message = "控制器配置不能为空")
    private AdminProperties admin;

    /**
     * 执行器配置
     */
    @NotNull(message = "执行器配置不能为空")
    private ExecutorProperties executor;

    /**
     * XXL-Job 调度器配置类
     */
    @Data
    @Valid
    public static class AdminProperties {

        @NotEmpty(message = "调度器地址不能为空")
        private String addresses;
    }

    /**
     * XXL-Job 执行器配置类
     */
    @Data
    public static class ExecutorProperties {

        /**
         * 默认端口
         * <p>
         * 这里使用 -1 表示随机
         */
        private static final Integer PORT_DEFAULT = -1;

        /**
         * 默认日志保留天数
         * <p>
         * 如果想永久保留，则设置为 -1
         */
        private static final Integer LOG_RETENTION_DAYS_DEFAULT = 30;

        /**
         * 应用名
         */
        private String appName;

        /**
         * 执行器的 IP
         */
        private String ip;

        /**
         * 执行器的 Port
         */
        private Integer port = PORT_DEFAULT;

        /**
         * 日志地址
         */
        private String logPath;

        /**
         * 日志保留天数
         */
        private Integer logRetentionDays = LOG_RETENTION_DAYS_DEFAULT;
    }
}
