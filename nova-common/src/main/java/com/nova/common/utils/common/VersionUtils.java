package com.nova.common.utils.common;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: wzh
 * @description: 版本对比工具类
 * @date: 2023/07/24 13:33
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VersionUtils {

    /**
     * 客户端版本 < 目标版本
     */
    public static boolean lt(String clientVersion, String targetVersion) {
        return compareVersion(targetVersion, clientVersion);
    }

    /**
     * 客户端版本 > 目标版本
     */
    public static boolean gt(String clientVersion, String targetVersion) {
        return compareVersion(clientVersion, targetVersion);
    }

    /**
     * 客户端版本 <= 目标版本
     */
    public static boolean le(String clientVersion, String targetVersion) {
        return StrUtil.equals(clientVersion, targetVersion) || lt(clientVersion, targetVersion);
    }

    /**
     * 客户端版本 >= 目标版本
     */
    public static boolean ge(String clientVersion, String targetVersion) {
        return StrUtil.equals(clientVersion, targetVersion) || gt(clientVersion, targetVersion);
    }

    /**
     * 版本号比较
     */
    public static boolean compareVersion(String newVersion, String clientVersion) {
        boolean result = false;
        try {
            if (StrUtil.isBlank(newVersion) || StrUtil.isBlank(clientVersion)) {
                return false;
            }
            String[] v1s = newVersion.split("\\.");
            String[] v2s = clientVersion.split("\\.");
            int mainVersion1 = Integer.parseInt(v1s[0]);
            int mainVersion2 = Integer.parseInt(v2s[0]);
            if (mainVersion1 == mainVersion2) {
                int subVersion1 = Integer.parseInt(v1s[1]);
                int subVersion2 = Integer.parseInt(v2s[1]);
                if (subVersion1 == subVersion2) {
                    int buildVersion1 = Integer.parseInt(v1s[2]);
                    int buildVersion2 = Integer.parseInt(v2s[2]);
                    return buildVersion1 > buildVersion2;
                } else {
                    result = subVersion1 > subVersion2;
                }
            } else {
                result = mainVersion1 > mainVersion2;
            }
        } catch (Exception e) {
            log.error("VersionUtils.compareVersion.error", e);
        }
        return result;
    }
}
