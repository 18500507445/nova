package com.nova.tools.utils.hutool.core.codec;

import cn.hutool.core.codec.Hashids;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class HashidsTest {
    @Test
    public void hexEncodeDecode() {
        final Hashids hashids = Hashids.create("my awesome salt".toCharArray());
        final String encoded1 = hashids.encodeFromHex("507f1f77bcf86cd799439011");
        final String encoded2 = hashids.encodeFromHex("0x507f1f77bcf86cd799439011");
        final String encoded3 = hashids.encodeFromHex("0X507f1f77bcf86cd799439011");

        Assert.equals("R2qnd2vkOJTXm7XV7yq4", encoded1);
        Assert.equals(encoded1, encoded2);
        Assert.equals(encoded1, encoded3);
        final String decoded = hashids.decodeToHex(encoded1);
        Assert.equals("507f1f77bcf86cd799439011", decoded);
    }
}
