package com.nova.tools.utils.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.Tailer;
import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

public class TailerTest {

    @Test
    public void tailTest() {
        FileUtil.tail(FileUtil.file("d:/test/tail.txt"), CharsetUtil.CHARSET_GBK);
    }

    @Test
    public void tailWithLinesTest() {
        Tailer tailer = new Tailer(FileUtil.file("f:/test/test.log"), Tailer.CONSOLE_HANDLER, 2);
        tailer.start();
    }
}
