package com.nova.tools.utils.hutool.json;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class Issue2365Test {

    @Test
    public void toBeanTest() {
        String jsonStr = "{\"fileName\":\"aaa\",\"fileBytes\":\"AQ==\"}";
        final FileInfo fileInfo = JSONUtil.toBean(jsonStr, FileInfo.class);
        Assert.equals("aaa", fileInfo.getFileName());
        Assert.equals(new byte[]{1}, fileInfo.getFileBytes());
    }

    @Data
    public static class FileInfo {
        private String fileName;
        private byte[] fileBytes;
    }
}
