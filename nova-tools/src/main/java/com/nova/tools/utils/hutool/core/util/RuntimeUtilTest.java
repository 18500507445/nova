package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RuntimeUtil;
import org.junit.jupiter.api.Test;

/**
 * {@link RuntimeUtil} 命令行单元测试
 *
 * @author
 */
public class RuntimeUtilTest {

    @Test
    public void execTest() {
        String str = RuntimeUtil.execForStr("ipconfig");
        Console.log(str);
    }

    @Test
    public void execCmdTest() {
        String str = RuntimeUtil.execForStr("cmd /c dir");
        Console.log(str);
    }

    @Test
    public void execCmdTest2() {
        String str = RuntimeUtil.execForStr("cmd /c", "cd \"C:\\Program Files (x86)\"", "chdir");
        Console.log(str);
    }

    @Test
    public void getUsableMemoryTest() {
        Assert.isTrue(RuntimeUtil.getUsableMemory() > 0);
    }

    @Test
    public void getPidTest() {
        int pid = RuntimeUtil.getPid();
        Assert.isTrue(pid > 0);
    }

    @Test
    public void getProcessorCountTest() {
        int cpu = RuntimeUtil.getProcessorCount();
        Console.log("cpu个数：{}", cpu);
        Assert.isTrue(cpu > 0);
    }
}
