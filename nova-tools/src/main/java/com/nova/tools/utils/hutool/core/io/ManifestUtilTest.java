package com.nova.tools.utils.hutool.core.io;

import cn.hutool.core.io.ManifestUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.jar.Manifest;

public class ManifestUtilTest {

    @Test
    public void getManiFestTest() {
        final Manifest manifest = ManifestUtil.getManifest(Test.class);
        Assert.notNull(manifest);
    }
}
