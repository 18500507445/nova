package com.nova.tools.utils.hutool.core.date;

import cn.hutool.core.date.Month;
import cn.hutool.core.date.Zodiac;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

/**
 * 通过生日计算星座
 */
public class ZodiacTest {

	@Test
	public void getZodiacTest() {
		Assert.equals("摩羯座", Zodiac.getZodiac(Month.JANUARY, 19));
		Assert.equals("水瓶座", Zodiac.getZodiac(Month.JANUARY, 20));
		Assert.equals("巨蟹座", Zodiac.getZodiac(6, 17));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		Assert.equals("巨蟹座", Zodiac.getZodiac(calendar.getTime()));
		Assert.equals("巨蟹座", Zodiac.getZodiac(calendar));
		Assert.isNull(Zodiac.getZodiac((Calendar) null));
	}

	@Test
	public void getChineseZodiacTest() {
		Assert.equals("狗", Zodiac.getChineseZodiac(1994));
		Assert.equals("狗", Zodiac.getChineseZodiac(2018));
		Assert.equals("猪", Zodiac.getChineseZodiac(2019));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		Assert.equals("虎", Zodiac.getChineseZodiac(calendar.getTime()));
		Assert.equals("虎", Zodiac.getChineseZodiac(calendar));
		Assert.isNull(Zodiac.getChineseZodiac(1899));
		Assert.isNull(Zodiac.getChineseZodiac((Calendar) null));
	}
}
