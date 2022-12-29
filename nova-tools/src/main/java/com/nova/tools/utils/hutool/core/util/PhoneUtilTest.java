package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * {@link PhoneUtil} 单元测试类
 *
 * @author dahuoyzs
 */
public class PhoneUtilTest {

	@Test
	public void testCheck() {
		final String mobile = "13612345678";
		final String tel = "010-88993108";
		final String errMobile = "136123456781";
		final String errTel = "010-889931081";

		Assert.isTrue(PhoneUtil.isMobile(mobile));
		Assert.isTrue(PhoneUtil.isTel(tel));
		Assert.isTrue(PhoneUtil.isPhone(mobile));
		Assert.isTrue(PhoneUtil.isPhone(tel));

		Assert.isFalse(PhoneUtil.isMobile(errMobile));
		Assert.isFalse(PhoneUtil.isTel(errTel));
		Assert.isFalse(PhoneUtil.isPhone(errMobile));
		Assert.isFalse(PhoneUtil.isPhone(errTel));
	}

	@Test
	public void testTel() {
		final ArrayList<String> tels = new ArrayList<>();
		tels.add("010-12345678");
		tels.add("020-9999999");
		tels.add("0755-7654321");
		final ArrayList<String> errTels = new ArrayList<>();
		errTels.add("010 12345678");
		errTels.add("A20-9999999");
		errTels.add("0755-7654.321");
		errTels.add("13619887123");
		for (final String s : tels) {
			Assert.isTrue(PhoneUtil.isTel(s));
		}
		for (final String s : errTels) {
			Assert.isFalse(PhoneUtil.isTel(s));
		}
	}

	@Test
	public void testHide() {
		final String mobile = "13612345678";

		Assert.equals("*******5678", PhoneUtil.hideBefore(mobile));
		Assert.equals("136****5678", PhoneUtil.hideBetween(mobile));
		Assert.equals("1361234****", PhoneUtil.hideAfter(mobile));
	}

	@Test
	public void testSubString() {
		final String mobile = "13612345678";
		Assert.equals("136", PhoneUtil.subBefore(mobile));
		Assert.equals("1234", PhoneUtil.subBetween(mobile));
		Assert.equals("5678", PhoneUtil.subAfter(mobile));
	}

	@Test
	public void testNewTel() {
		final ArrayList<String> tels = new ArrayList<>();
		tels.add("010-12345678");
		tels.add("01012345678");
		tels.add("020-9999999");
		tels.add("0209999999");
		tels.add("0755-7654321");
		tels.add("07557654321");
		final ArrayList<String> errTels = new ArrayList<>();
		errTels.add("010 12345678");
		errTels.add("A20-9999999");
		errTels.add("0755-7654.321");
		errTels.add("13619887123");
		for (final String s : tels) {
			Assert.isTrue(PhoneUtil.isTel(s));
		}
		for (final String s : errTels) {
			Assert.isFalse(PhoneUtil.isTel(s));
		}
		Assert.equals("010", PhoneUtil.subTelBefore("010-12345678"));
		Assert.equals("010", PhoneUtil.subTelBefore("01012345678"));
		Assert.equals("12345678", PhoneUtil.subTelAfter("010-12345678"));
		Assert.equals("12345678", PhoneUtil.subTelAfter("01012345678"));

		Assert.equals("0755", PhoneUtil.subTelBefore("0755-7654321"));
		Assert.equals("0755", PhoneUtil.subTelBefore("07557654321"));
		Assert.equals("7654321", PhoneUtil.subTelAfter("0755-7654321"));
		Assert.equals("7654321", PhoneUtil.subTelAfter("07557654321"));
	}
}
