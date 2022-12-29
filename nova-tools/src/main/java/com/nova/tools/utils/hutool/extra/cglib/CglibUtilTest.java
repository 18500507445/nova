package com.nova.tools.utils.hutool.extra.cglib;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.cglib.CglibUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class CglibUtilTest {

	@Test
	public void copyTest() {
		SampleBean bean = new SampleBean();
		OtherSampleBean otherBean = new OtherSampleBean();
		bean.setValue("Hello world");
		bean.setValue2("123");

		CglibUtil.copy(bean, otherBean);
		Assert.equals("Hello world", otherBean.getValue());
		// 无定义转换器，转换失败
		Assert.equals(0, otherBean.getValue2());

		OtherSampleBean otherBean2 = CglibUtil.copy(bean, OtherSampleBean.class);
		Assert.equals("Hello world", otherBean2.getValue());
		// 无定义转换器，转换失败
		Assert.equals(0, otherBean.getValue2());
	}

	@Data
	public static class SampleBean {
		private String value;
		private String value2;
	}

	@Data
	public static class OtherSampleBean {
		private String value;
		private int value2;
	}
}
