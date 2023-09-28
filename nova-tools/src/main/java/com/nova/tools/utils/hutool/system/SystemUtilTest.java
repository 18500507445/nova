package com.nova.tools.utils.hutool.system;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.system.*;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SystemUtilTest {

    @Test
    public void dumpTest() {
        SystemUtil.dumpSystemInfo();
    }

    @Test
    public void getCurrentPidTest() {
        final long pid = SystemUtil.getCurrentPID();
        Assert.isTrue(pid > 0);
    }

    @Test
    public void getJavaInfoTest() {
        final JavaInfo javaInfo = SystemUtil.getJavaInfo();
        Assert.notNull(javaInfo);
    }

    @Test
    public void getJavaRuntimeInfoTest() {
        final JavaRuntimeInfo info = SystemUtil.getJavaRuntimeInfo();
        Assert.notNull(info);
    }

    @Test
    public void getOsInfoTest() {
        final OsInfo osInfo = SystemUtil.getOsInfo();
        Assert.notNull(osInfo);

        Console.log(osInfo.getName());
    }

    @Test
    public void getHostInfo() {
        final HostInfo hostInfo = SystemUtil.getHostInfo();
        Assert.notNull(hostInfo);
    }

    @Test
    public void getUserInfoTest() {
        // https://gitee.com/dromara/hutool/issues/I3NM39
        final UserInfo userInfo = SystemUtil.getUserInfo();
        Assert.isTrue(userInfo.getTempDir().endsWith(File.separator));
    }
}
