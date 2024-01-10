package com.nova.starter.xxl;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/22 22:10
 */
// 指定了前缀为"xxl.job"的配置属性，可以再其它类里进行注入使用了
@ConfigurationProperties("xxl.job")
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
    private AdminProperties admin;

    /**
     * 执行器配置
     */
    private ExecutorProperties executor;

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAdmin(AdminProperties admin) {
        this.admin = admin;
    }

    public void setExecutor(ExecutorProperties executor) {
        this.executor = executor;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AdminProperties getAdmin() {
        return admin;
    }

    public ExecutorProperties getExecutor() {
        return executor;
    }

    /**
     * XXL-Job 调度器配置类
     */
    public static class AdminProperties {

        /**
         * 调度器地址
         */
        private String addresses;

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }

        public String getAddresses() {
            return addresses;
        }
    }

    /**
     * XXL-Job 执行器配置类
     */
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

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public void setLogRetentionDays(Integer logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }

        public String getAppName() {
            return appName;
        }

        public String getIp() {
            return ip;
        }

        public Integer getPort() {
            return port;
        }

        public String getLogPath() {
            return logPath;
        }

        public Integer getLogRetentionDays() {
            return logRetentionDays;
        }
    }
}
