package com.nova.tools.utils.hutool.core.swing;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.RobotUtil;
import org.junit.jupiter.api.Test;

public class RobotUtilTest {

    @Test
    public void captureScreenTest() {
        RobotUtil.captureScreen(FileUtil.file("e:/screen.jpg"));
    }
}
