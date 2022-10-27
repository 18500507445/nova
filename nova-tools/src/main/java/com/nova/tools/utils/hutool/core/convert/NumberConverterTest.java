package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.impl.NumberConverter;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class NumberConverterTest {

	@Test
	public void toDoubleTest(){
		final NumberConverter numberConverter = new NumberConverter(Double.class);
		final Number convert = numberConverter.convert("1,234.55", null);
		Assert.equals(1234.55D, convert);
	}

	@Test
	public void toIntegerTest(){
		final NumberConverter numberConverter = new NumberConverter(Integer.class);
		final Number convert = numberConverter.convert("1,234.55", null);
		Assert.equals(1234, convert);
	}
}
