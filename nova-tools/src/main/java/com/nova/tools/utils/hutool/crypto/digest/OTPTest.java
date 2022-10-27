package com.nova.tools.utils.hutool.crypto.digest;

import cn.hutool.core.codec.Base32;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.otp.HOTP;
import cn.hutool.crypto.digest.otp.TOTP;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

/**
 * author: xlgogo@outlook.com
 * date: 2021-07-01 18:14
 */
public class OTPTest {

	@Test
	public void genKeyTest() {
		String key = TOTP.generateSecretKey(8);
		Assert.equals(8, Base32.decode(key).length);
	}

	@Test
	public void validTest() {
		String key = "VYCFSW2QZ3WZO";
		// 2021/7/1下午6:29:54 显示code为 106659
		//Assert.equals(new TOTP(Base32.decode(key)).generate(Instant.ofEpochSecond(1625135394L)),106659);
		TOTP totp = new TOTP(Base32.decode(key));
		Instant instant = Instant.ofEpochSecond(1625135394L);
		Assert.isTrue(totp.validate(instant, 0, 106659));
		Assert.isTrue(totp.validate(instant.plusSeconds(30), 1, 106659));
		Assert.isTrue(totp.validate(instant.plusSeconds(60), 2, 106659));

		Assert.isFalse(totp.validate(instant.plusSeconds(60), 1, 106659));
		Assert.isFalse(totp.validate(instant.plusSeconds(90), 2, 106659));
	}

	@Test
	public void googleAuthTest() {
		String str = TOTP.generateGoogleSecretKey("xl7@qq.com", 10);
		Assert.isTrue(str.startsWith("otpauth://totp/xl7@qq.com?secret="));
	}

	@Test
	public void longPasswordLengthTest() {
		//Assert.assertThrows(IllegalArgumentException.class, () -> new HOTP(9, "123".getBytes()));
	}

	@Test
	public void generateHOPTTest(){
		byte[] key = "12345678901234567890".getBytes();
		final HOTP hotp = new HOTP(key);
		Assert.equals(755224, hotp.generate(0));
		Assert.equals(287082, hotp.generate(1));
		Assert.equals(359152, hotp.generate(2));
		Assert.equals(969429, hotp.generate(3));
		Assert.equals(338314, hotp.generate(4));
		Assert.equals(254676, hotp.generate(5));
		Assert.equals(287922, hotp.generate(6));
		Assert.equals(162583, hotp.generate(7));
		Assert.equals(399871, hotp.generate(8));
		Assert.equals(520489, hotp.generate(9));
	}

	@Test
	public void getTimeStepTest() {
		final Duration timeStep = Duration.ofSeconds(97);

		final TOTP totp = new TOTP(timeStep, "123".getBytes());

		Assert.equals(timeStep, totp.getTimeStep());
	}

	@Test
	public void generateHmacSHA1TOPTTest(){
		HmacAlgorithm algorithm = HmacAlgorithm.HmacSHA1;
		byte[] key = "12345678901234567890".getBytes();
		TOTP totp = new TOTP(Duration.ofSeconds(30), 8, algorithm, key);

		int generate = totp.generate(Instant.ofEpochSecond(59L));
		Assert.equals(94287082, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111109L));
		Assert.equals(7081804, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111111L));
		Assert.equals(14050471, generate);
		generate = totp.generate(Instant.ofEpochSecond(1234567890L));
		Assert.equals(89005924, generate);
		generate = totp.generate(Instant.ofEpochSecond(2000000000L));
		Assert.equals(69279037, generate);
		generate = totp.generate(Instant.ofEpochSecond(20000000000L));
		Assert.equals(65353130, generate);
	}

	@Test
	public void generateHmacSHA256TOPTTest(){
		HmacAlgorithm algorithm = HmacAlgorithm.HmacSHA256;
		byte[] key = "12345678901234567890123456789012".getBytes();
		TOTP totp = new TOTP(Duration.ofSeconds(30), 8, algorithm, key);

		int generate = totp.generate(Instant.ofEpochSecond(59L));
		Assert.equals(46119246, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111109L));
		Assert.equals(68084774, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111111L));
		Assert.equals(67062674, generate);
		generate = totp.generate(Instant.ofEpochSecond(1234567890L));
		Assert.equals(91819424, generate);
		generate = totp.generate(Instant.ofEpochSecond(2000000000L));
		Assert.equals(90698825, generate);
		generate = totp.generate(Instant.ofEpochSecond(20000000000L));
		Assert.equals(77737706, generate);
	}

	@Test
	public void generateHmacSHA512TOPTTest(){
		HmacAlgorithm algorithm = HmacAlgorithm.HmacSHA512;
		byte[] key = "1234567890123456789012345678901234567890123456789012345678901234".getBytes();
		TOTP totp = new TOTP(Duration.ofSeconds(30), 8, algorithm, key);

		int generate = totp.generate(Instant.ofEpochSecond(59L));
		Assert.equals(90693936, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111109L));
		Assert.equals(25091201, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111111L));
		Assert.equals(99943326, generate);
		generate = totp.generate(Instant.ofEpochSecond(1234567890L));
		Assert.equals(93441116, generate);
		generate = totp.generate(Instant.ofEpochSecond(2000000000L));
		Assert.equals(38618901, generate);
		generate = totp.generate(Instant.ofEpochSecond(20000000000L));
		Assert.equals(47863826, generate);
	}
}
