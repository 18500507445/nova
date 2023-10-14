package com.nova.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wzh
 * @Title: Global
 * @ProjectName crazy
 * @description: 全局配置类
 * @date 2021/7/18 16:19
 */
@Component
@ConfigurationProperties(prefix = "nova")
public class Global {

    /**
     * 项目名称
     */
    @Getter
    @Setter
    private static String name;

    /**
     * 版本
     */
    @Getter
    @Setter
    private static String version;

    /**
     * 版权年份
     */
    @Getter
    @Setter
    private static String copyrightYear;

    /**
     * 实例演示开关
     */
    @Getter
    @Setter
    private static boolean demoEnabled;

    /**
     * 上传路径
     */
    @Getter
    @Setter
    private static String profile;

    /**
     * 获取地址开关
     */
    @Getter
    @Setter
    private static boolean addressEnabled;

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }

}
