package com.nova.tools.utils.hutool.core.swing;

import cn.hutool.core.swing.DesktopUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

public class DesktopUtilTest {
	
	@Test
	@Ignore
	public void browseTest() {
		DesktopUtil.browse("https://www.hutool.club");
	}
}
