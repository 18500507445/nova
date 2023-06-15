package com.nova.tools.utils.hutool.core.codec;

import cn.hutool.core.codec.Morse;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class MorseTest {

    private final Morse morseCoder = new Morse();

    @Test
    public void test0() {
        String text = "Hello World!";
        String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
        Assert.equals(morse, morseCoder.encode(text));
        Assert.equals(morseCoder.decode(morse), text.toUpperCase());
    }

    @Test
    public void test1() {
        String text = "你好，世界！";
        String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
        Assert.equals(morseCoder.encode(text), morse);
        Assert.equals(morseCoder.decode(morse), text);
    }

    @Test
    public void test2() {
        String text = "こんにちは";
        String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
        Assert.equals(morseCoder.encode(text), morse);
        Assert.equals(morseCoder.decode(morse), text);
    }
}
