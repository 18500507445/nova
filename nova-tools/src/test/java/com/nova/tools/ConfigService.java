package com.nova.tools;

/**
 * @author: wzh
 * @description 配置service
 * @date: 2023/10/31 15:54
 */
public interface ConfigService {

    String get(String key);

    /**
     * 刷新缓存
     */
    void refreshRedis(String... key);
}
