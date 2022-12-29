package com.nova.tools.utils.hutool.core.net;

import cn.hutool.core.net.FormUrlencoded;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class FormUrlencodedTest {

	@Test
	public void encodeParamTest(){
		String encode = FormUrlencoded.ALL.encode("a+b", CharsetUtil.CHARSET_UTF_8);
		Assert.equals("a%2Bb", encode);

		encode = FormUrlencoded.ALL.encode("a b", CharsetUtil.CHARSET_UTF_8);
		Assert.equals("a+b", encode);
	}
}
