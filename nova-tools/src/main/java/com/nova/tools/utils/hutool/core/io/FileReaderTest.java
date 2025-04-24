package com.nova.tools.utils.hutool.core.io;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 文件读取测试
 *
 * @author: Looly
 */
public class FileReaderTest {

    @Test
    public void fileReaderTest() {
        FileReader fileReader = new FileReader("test.properties");
        String result = fileReader.readString();
        Assert.notNull(result);
    }
}
