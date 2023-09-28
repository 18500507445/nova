package com.nova.tools.utils.hutool.core.swing;

import cn.hutool.core.swing.DesktopUtil;
import org.junit.jupiter.api.Test;

public class DesktopUtilTest {

    @Test
    public void browseTest() {
        DesktopUtil.browse("https://www.hutool.club");
    }
}
