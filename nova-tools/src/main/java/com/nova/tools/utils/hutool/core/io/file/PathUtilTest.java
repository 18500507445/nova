package com.nova.tools.utils.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PathUtilTest {

    @Test
    public void copyFileTest() {
        PathUtil.copyFile(
                Paths.get("d:/test/1595232240113.jpg"),
                Paths.get("d:/test/1595232240113_copy.jpg"),
                StandardCopyOption.COPY_ATTRIBUTES,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    @Test
    public void copyTest() {
        PathUtil.copy(
                Paths.get("d:/Red2_LYY"),
                Paths.get("d:/test/aaa/aaa.txt")
        );
    }

    @Test
    public void copyContentTest() {
        PathUtil.copyContent(
                Paths.get("d:/Red2_LYY"),
                Paths.get("d:/test/aaa/")
        );
    }

    @Test
    public void moveTest() {
        PathUtil.move(Paths.get("d:/lombok.jar"), Paths.get("d:/test/"), false);
    }

    @Test
    public void moveDirTest() {
        PathUtil.move(Paths.get("c:\\aaa"), Paths.get("d:/test/looly"), false);
    }

    @Test
    public void delDirTest() {
        PathUtil.del(Paths.get("d:/test/looly"));
    }

    @Test
    public void getMimeTypeTest() {
        String mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.jpg"));
        Assert.equals("image/jpeg", mimeType);

        mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.mov"));
        Assert.equals("video/quicktime", mimeType);
    }

    @Test
    public void getMimeOfRarTest() {
        String contentType = FileUtil.getMimeType("a001.rar");
        Assert.equals("application/x-rar-compressed", contentType);
    }

    @Test
    public void getMimeOf7zTest() {
        String contentType = FileUtil.getMimeType("a001.7z");
        Assert.equals("application/x-7z-compressed", contentType);
    }
}
