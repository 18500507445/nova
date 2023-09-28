package com.nova.tools.utils.hutool.system;

import cn.hutool.core.lang.Console;
import cn.hutool.system.oshi.OshiUtil;
import org.junit.jupiter.api.Test;


public class OshiPrintTest {

    @Test
    public void printCpuInfo() {
        Console.log(OshiUtil.getCpuInfo());
    }
}
