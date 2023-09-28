package com.nova.tools.utils.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.CompressUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import org.junit.jupiter.api.Test;

public class ExtractorTest {

    @Test
    public void zipTest() {
        Extractor extractor = CompressUtil.createExtractor(
                CharsetUtil.defaultCharset(),
                FileUtil.file("d:/test/c_1344112734760931330_20201230104703032.zip"));

        extractor.extract(FileUtil.file("d:/test/compress/test2/"));
    }

    @Test
    public void sevenZTest() {
        Extractor extractor = CompressUtil.createExtractor(
                CharsetUtil.defaultCharset(),
                FileUtil.file("d:/test/compress/test.7z"));

        extractor.extract(FileUtil.file("d:/test/compress/test2/"));
    }

    @Test
    public void tgzTest() {
        Extractor extractor = CompressUtil.createExtractor(
                CharsetUtil.defaultCharset(),
                "tgz",
                FileUtil.file("d:/test/test.tgz"));

        extractor.extract(FileUtil.file("d:/test/tgz/"));
    }
}
