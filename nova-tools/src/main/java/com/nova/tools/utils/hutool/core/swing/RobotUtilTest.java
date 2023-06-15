package com.nova.tools.utils.hutool.core.swing;

import cn.hutool.core.swing.RobotUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import cn.hutool.core.io.FileUtil;

public class RobotUtilTest {

    @Test
    @Ignore
    public void captureScreenTest() {
        RobotUtil.captureScreen(FileUtil.file("e:/screen.jpg"));
    }
}
