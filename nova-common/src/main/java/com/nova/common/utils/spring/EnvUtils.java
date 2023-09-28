package com.nova.common.utils.spring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;

/**
 * @author: wzh
 * @description 判断环境工具类
 * @date: 2023/09/28 11:12
 */
public class EnvUtils {

    private EnvUtils() {

    }

    /**
     * 是否是测试
     *
     * @return true 测试环境
     */
    public static boolean isDev() {
        return StrUtil.equals(SpringUtil.getActiveProfile(), "dev");
    }

    /**
     * 是否是正式
     *
     * @return true 正式环境
     */
    public static boolean isPro() {
        return StrUtil.equals(SpringUtil.getActiveProfile(), "pro");
    }

    /**
     * 是否是本地
     *
     * @return true 本地环境
     */
    public static boolean isLocal() {
        return StrUtil.equals(SpringUtil.getActiveProfile(), "local");
    }

    /**
     * 是否是后台
     *
     * @return true 后台环境
     */
    public static boolean isAdmin() {
        return StrUtil.equals(SpringUtil.getActiveProfile(), "admin");
    }
}
