package com.nova.tools.utils.hutool.cache;

import cn.hutool.cache.file.LFUFileCache;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 文件缓存单元测试
 *
 * @author looly
 */
public class FileCacheTest {

    @Test
    public void lfuFileCacheTest() {
        LFUFileCache cache = new LFUFileCache(1000, 500, 2000);
        Assert.notNull(cache);
    }
}
