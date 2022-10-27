package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.NumberWordFormatter;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class NumberWordFormatTest {

	@Test
	public void formatTest() {
		String format = NumberWordFormatter.format(100.23);
		Assert.equals("ONE HUNDRED AND CENTS TWENTY THREE ONLY", format);

		String format2 = NumberWordFormatter.format("2100.00");
		Assert.equals("TWO THOUSAND ONE HUNDRED AND CENTS  ONLY", format2);
	}

	@Test
	public void formatSimpleTest() {
		String format1 = NumberWordFormatter.formatSimple(1200, false);
		Assert.equals("1.2k", format1);

		String format2 = NumberWordFormatter.formatSimple(4384324, false);
		Assert.equals("4.38m", format2);

		String format3 = NumberWordFormatter.formatSimple(4384324, true);
		Assert.equals("438.43w", format3);

		String format4 = NumberWordFormatter.formatSimple(4384324);
		Assert.equals("438.43w", format4);

		String format5 = NumberWordFormatter.formatSimple(438);
		Assert.equals("438", format5);
	}

	@Test
	public void formatSimpleTest2(){
		final String s = NumberWordFormatter.formatSimple(1000);
		Assert.equals("1k", s);
	}
}
