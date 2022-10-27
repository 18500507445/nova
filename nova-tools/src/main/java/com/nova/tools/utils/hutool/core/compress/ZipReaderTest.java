package com.nova.tools.utils.hutool.core.compress;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ZipUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ZipReaderTest {

	@Test
	@Ignore
	public void unzipTest() {
		File unzip = ZipUtil.unzip("d:/java.zip", "d:/test/java");
		Console.log(unzip);
	}
}
