package com.nova.tools.utils.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.compress.CompressUtil;
import cn.hutool.extra.compress.archiver.StreamArchiver;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.jupiter.api.Test;

import java.io.File;

@SuppressWarnings("resource")
public class ArchiverTest {

    @Test
    public void zipTest() {
        final File file = FileUtil.file("d:/test/compress/test.zip");
        StreamArchiver.create(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.ZIP, file)
                .add(FileUtil.file("d:/Java"), (f) -> {
                    Console.log("Add: {}", f.getPath());
                    return true;
                })
                .finish().close();
    }

    @Test
    public void tarTest() {
        final File file = FileUtil.file("d:/test/compress/test.tar");
        StreamArchiver.create(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.TAR, file)
                .add(FileUtil.file("d:/Java"), (f) -> {
                    Console.log("Add: {}", f.getPath());
                    return true;
                })
                .finish().close();
    }

    @Test
    public void cpioTest() {
        final File file = FileUtil.file("d:/test/compress/test.cpio");
        StreamArchiver.create(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.CPIO, file)
                .add(FileUtil.file("d:/Java"), (f) -> {
                    Console.log("Add: {}", f.getPath());
                    return true;
                })
                .finish().close();
    }

    @Test
    public void sevenZTest() {
        final File file = FileUtil.file("d:/test/compress/test.7z");
        CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, ArchiveStreamFactory.SEVEN_Z, file)
                .add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f) -> {
                    Console.log("Add: {}", f.getPath());
                    return true;
                })
                .finish().close();
    }

    @Test
    public void tgzTest() {
        final File file = FileUtil.file("d:/test/compress/test.tgz");
        CompressUtil.createArchiver(CharsetUtil.CHARSET_UTF_8, "tgz", file)
                .add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f) -> {
                    Console.log("Add: {}", f.getPath());
                    return true;
                })
                .finish().close();
    }
}
