package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConvertTest {

	@Test
	public void toDateTest() {
		String a = "2017-05-06";
		Date value = Convert.toDate(a);
		Assert.equals(a, DateUtil.formatDate(value));

		long timeLong = DateUtil.date().getTime();
		Date value2 = Convert.toDate(timeLong);
		Assert.equals(timeLong, value2.getTime());
	}

	@Test
	public void toDateFromIntTest() {
		int dateLong = -1497600000;
		Date value = Convert.toDate(dateLong);
		Assert.notNull(value);
		Assert.equals("Mon Dec 15 00:00:00 CST 1969", value.toString().replace("GMT+08:00", "CST"));

		final java.sql.Date sqlDate = Convert.convert(java.sql.Date.class, dateLong);
		Assert.notNull(sqlDate);
		Assert.equals("1969-12-15", sqlDate.toString());
	}

	@Test
	public void toDateFromLocalDateTimeTest() {
		LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		Date value = Convert.toDate(localDateTime);
		Assert.notNull(value);
		Assert.equals("2017-05-06", DateUtil.formatDate(value));
	}

	@Test
	public void toSqlDateTest() {
		String a = "2017-05-06";
		java.sql.Date value = Convert.convert(java.sql.Date.class, a);
		Assert.equals("2017-05-06", value.toString());

		long timeLong = DateUtil.date().getTime();
		java.sql.Date value2 = Convert.convert(java.sql.Date.class, timeLong);
		Assert.equals(timeLong, value2.getTime());
	}

	@Test
	public void toLocalDateTimeTest() {
		Date src = new Date();

		LocalDateTime ldt = Convert.toLocalDateTime(src);
		Assert.equals(ldt, DateUtil.toLocalDateTime(src));

		Timestamp ts = Timestamp.from(src.toInstant());
		ldt = Convert.toLocalDateTime(ts);
		Assert.equals(ldt, DateUtil.toLocalDateTime(src));

		String str = "2020-12-12 12:12:12.0";
		ldt = Convert.toLocalDateTime(str);
		Assert.equals(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")), str);
	}
}
