package com.nova.tools.utils.hutool.core.lang;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * 验证器单元测试
 *
 * @author Looly
 */
public class ValidatorTest {

	@Test
	public void isNumberTest() {
		Assert.isTrue(Validator.isNumber("45345365465"));
		Assert.isTrue(Validator.isNumber("0004545435"));
		Assert.isTrue(Validator.isNumber("5.222"));
		Assert.isTrue(Validator.isNumber("0.33323"));
	}

	@Test
	public void hasNumberTest() {
		String var1 = "";
		String var2 = "str";
		String var3 = "180";
		String var4 = "身高180体重180";
		Assert.isFalse(Validator.hasNumber(var1));
		Assert.isFalse(Validator.hasNumber(var2));
		Assert.isTrue(Validator.hasNumber(var3));
		Assert.isTrue(Validator.hasNumber(var4));
	}

	@Test
	public void isLetterTest() {
		Assert.isTrue(Validator.isLetter("asfdsdsfds"));
		Assert.isTrue(Validator.isLetter("asfdsdfdsfVCDFDFGdsfds"));
		Assert.isTrue(Validator.isLetter("asfdsdf你好dsfVCDFDFGdsfds"));
	}

	@Test
	public void isUperCaseTest() {
		Assert.isTrue(Validator.isUpperCase("VCDFDFG"));
		Assert.isTrue(Validator.isUpperCase("ASSFD"));

		Assert.isFalse(Validator.isUpperCase("asfdsdsfds"));
		Assert.isFalse(Validator.isUpperCase("ASSFD你好"));
	}

	@Test
	public void isLowerCaseTest() {
		Assert.isTrue(Validator.isLowerCase("asfdsdsfds"));

		Assert.isFalse(Validator.isLowerCase("aaaa你好"));
		Assert.isFalse(Validator.isLowerCase("VCDFDFG"));
		Assert.isFalse(Validator.isLowerCase("ASSFD"));
		Assert.isFalse(Validator.isLowerCase("ASSFD你好"));
	}

	@Test
	public void isBirthdayTest() {
		boolean b = Validator.isBirthday("20150101");
		Assert.isTrue(b);
		boolean b2 = Validator.isBirthday("2015-01-01");
		Assert.isTrue(b2);
		boolean b3 = Validator.isBirthday("2015.01.01");
		Assert.isTrue(b3);
		boolean b4 = Validator.isBirthday("2015年01月01日");
		Assert.isTrue(b4);
		boolean b5 = Validator.isBirthday("2015.01.01");
		Assert.isTrue(b5);
		boolean b6 = Validator.isBirthday("2018-08-15");
		Assert.isTrue(b6);

		//验证年非法
		Assert.isFalse(Validator.isBirthday("2095.05.01"));
		//验证月非法
		Assert.isFalse(Validator.isBirthday("2015.13.01"));
		//验证日非法
		Assert.isFalse(Validator.isBirthday("2015.02.29"));
	}

	@Test
	public void isCitizenIdTest() {
		// 18为身份证号码验证
		boolean b = Validator.isCitizenId("110101199003074477");
		Assert.isTrue(b);

		// 15位身份证号码验证
		boolean b1 = Validator.isCitizenId("410001910101123");
		Assert.isTrue(b1);

		// 10位身份证号码验证
		boolean b2 = Validator.isCitizenId("U193683453");
		Assert.isTrue(b2);
	}

	@Test
	public void validateTest() throws ValidateException {
		Validator.validateChinese("我是一段zhongwen", "内容中包含非中文");
	}

	@Test
	public void isEmailTest() {
		boolean email = Validator.isEmail("abc_cde@163.com");
		Assert.isTrue(email);
		boolean email1 = Validator.isEmail("abc_%cde@163.com");
		Assert.isTrue(email1);
		boolean email2 = Validator.isEmail("abc_%cde@aaa.c");
		Assert.isTrue(email2);
		boolean email3 = Validator.isEmail("xiaolei.lu@aaa.b");
		Assert.isTrue(email3);
		boolean email4 = Validator.isEmail("xiaolei.Lu@aaa.b");
		Assert.isTrue(email4);
	}

	@Test
	public void isMobileTest() {
		boolean m1 = Validator.isMobile("13900221432");
		Assert.isTrue(m1);
		boolean m2 = Validator.isMobile("015100221432");
		Assert.isTrue(m2);
		boolean m3 = Validator.isMobile("+8618600221432");
		Assert.isTrue(m3);
	}

	@Test
	public void isMatchTest() {
		String url = "http://aaa-bbb.somthing.com/a.php?a=b&c=2";
		Assert.isTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com/a.php?a=b&c=2";
		Assert.isTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));

		url = "https://aaa-bbb.somthing.com:8080/a.php?a=b&c=2";
		Assert.isTrue(Validator.isMatchRegex(PatternPool.URL_HTTP, url));
	}

	@Test
	public void isGeneralTest() {
		String str = "";
		boolean general = Validator.isGeneral(str, -1, 5);
		Assert.isTrue(general);

		str = "123_abc_ccc";
		general = Validator.isGeneral(str, -1, 100);
		Assert.isTrue(general);

		// 不允许中文
		str = "123_abc_ccc中文";
		general = Validator.isGeneral(str, -1, 100);
		Assert.isFalse(general);
	}

	@Test
	public void isPlateNumberTest(){
		Assert.isTrue(Validator.isPlateNumber("粤BA03205"));
		Assert.isTrue(Validator.isPlateNumber("闽20401领"));
	}

	@Test
	public void isChineseTest(){
		Assert.isTrue(Validator.isChinese("全都是中文"));
		Assert.isTrue(Validator.isChinese("㐓㐘"));
		Assert.isFalse(Validator.isChinese("not全都是中文"));
	}

	@Test
	public void hasChineseTest() {
		Assert.isTrue(Validator.hasChinese("黄单桑米"));
		Assert.isTrue(Validator.hasChinese("Kn 四兄弟"));
		Assert.isTrue(Validator.hasChinese("\uD840\uDDA3"));
		Assert.isFalse(Validator.hasChinese("Abc"));
	}

	@Test
	public void isUUIDTest(){
		Assert.isTrue(Validator.isUUID(IdUtil.randomUUID()));
		Assert.isTrue(Validator.isUUID(IdUtil.fastSimpleUUID()));

		Assert.isTrue(Validator.isUUID(IdUtil.randomUUID().toUpperCase()));
		Assert.isTrue(Validator.isUUID(IdUtil.fastSimpleUUID().toUpperCase()));
	}

	@Test
	public void isZipCodeTest(){
		// 港
		boolean zipCode = Validator.isZipCode("999077");
		Assert.isTrue(zipCode);
		// 澳
		zipCode = Validator.isZipCode("999078");
		Assert.isTrue(zipCode);
		// 台（2020年3月起改用6位邮编，3+3）
		zipCode = Validator.isZipCode("822001");
		Assert.isTrue(zipCode);

		// 内蒙
		zipCode = Validator.isZipCode("016063");
		Assert.isTrue(zipCode);
		// 山西
		zipCode = Validator.isZipCode("045246");
		Assert.isTrue(zipCode);
		// 河北
		zipCode = Validator.isZipCode("066502");
		Assert.isTrue(zipCode);
		// 北京
		zipCode = Validator.isZipCode("102629");
		Assert.isTrue(zipCode);
	}

	@Test
	public void isBetweenTest() {
		Assert.isTrue(Validator.isBetween(0, 0, 1));
		Assert.isTrue(Validator.isBetween(1L, 0L, 1L));
		Assert.isTrue(Validator.isBetween(0.19f, 0.1f, 0.2f));
		Assert.isTrue(Validator.isBetween(0.19, 0.1, 0.2));
	}

	@Test
	public void isCarVinTest(){
		Assert.isTrue(Validator.isCarVin("LSJA24U62JG269225"));
		Assert.isTrue(Validator.isCarVin("LDC613P23A1305189"));
		Assert.isFalse(Validator.isCarVin("LOC613P23A1305189"));
	}

	@Test
	public void isCarDrivingLicenceTest(){
		Assert.isTrue(Validator.isCarDrivingLicence("430101758218"));
	}

	@Test
	public void validateIpv4Test(){
		Validator.validateIpv4("192.168.1.1", "Error ip");
		Validator.validateIpv4("8.8.8.8", "Error ip");
		Validator.validateIpv4("0.0.0.0", "Error ip");
		Validator.validateIpv4("255.255.255.255", "Error ip");
		Validator.validateIpv4("127.0.0.0", "Error ip");
	}

	@Test
	public void isUrlTest(){
		String content = "https://detail.tmall.com/item.htm?" +
				"id=639428931841&ali_refid=a3_430582_1006:1152464078:N:Sk5vwkMVsn5O6DcnvicELrFucL21A32m:0af8611e23c1d07697e";

		Assert.isTrue(Validator.isMatchRegex(Validator.URL, content));
		Assert.isTrue(Validator.isMatchRegex(Validator.URL_HTTP, content));
	}

	@Test
	public void isChineseNameTest(){
		Assert.isTrue(Validator.isChineseName("阿卜杜尼亚孜·毛力尼亚孜"));
		Assert.isFalse(Validator.isChineseName("阿卜杜尼亚孜./毛力尼亚孜"));
		Assert.isTrue(Validator.isChineseName("段正淳"));
		Assert.isFalse(Validator.isChineseName("孟  伟"));
		Assert.isFalse(Validator.isChineseName("李"));
		Assert.isFalse(Validator.isChineseName("连逍遥0"));
		Assert.isFalse(Validator.isChineseName("SHE"));
	}
}
