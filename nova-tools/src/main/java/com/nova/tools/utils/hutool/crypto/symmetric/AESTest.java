package com.nova.tools.utils.hutool.crypto.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;

/**
 * {@link AES}
 */
public class AESTest {

	@Test
	public void encryptCBCTest() {
		// 构建
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,
				"1234567890123456".getBytes(), "1234567890123456".getBytes());
		String encryptHex = aes.encryptHex("123456");
		Assert.equals("d637735ae9e21ba50cb686b74fab8d2c", encryptHex);
	}

	@Test
	public void encryptCTSTest() {
		String content = "test中文";
		AES aes = new AES(Mode.CTS, Padding.PKCS5Padding,
				"0CoJUm6Qyw8W8jue".getBytes(), "0102030405060708".getBytes());
		final String encryptHex = aes.encryptHex(content);
		Assert.equals("8dc9de7f050e86ca2c8261dde56dfec9", encryptHex);
	}

	@Test
	public void encryptPKCS7Test() {
		// 构建
		AES aes = new AES(Mode.CBC.name(), "pkcs7padding",
				"1234567890123456".getBytes(), "1234567890123456".getBytes());
		String encryptHex = aes.encryptHex("123456");
		Assert.equals("d637735ae9e21ba50cb686b74fab8d2c", encryptHex);
	}

	/**
	 * AES加密/解密
	 * 加解密模式:ECB模式 数据填充模式:PKCS7
	 * <p>
	 * 数据：16c5
	 * 密钥: 0102030405060708090a0b0c0d0e0f10
	 * 数据格式:hex格式 加解密模式:ECB模式 数据填充模式:PKCS7
	 * 结果: 25869eb3ff227d9e34b3512d3c3c92ed 【加密后的Hex】
	 * 结果: JYaes/8ifZ40s1EtPDyS7Q== 【加密后的Base64】
	 * <p>
	 * 数据：16c5
	 * 密钥: 0102030405060708090a0b0c0d0e0f10
	 * 数据格式:UTF-8格式 加解密模式:ECB模式 数据填充模式:PKCS7
	 * 结果: 79c210d3e304932cf9ea6a9c887c6d7c 【加密后的Hex】
	 * 结果: ecIQ0+MEkyz56mqciHxtfA== 【加密后的Base64】
	 * <p>
	 * AES在线解密 AES在线加密 Aes online hex 十六进制密钥 - The X 在线工具
	 * https://the-x.cn/cryptography/Aes.aspx
	 */
	@Test
	public void encryptPKCS7Test2() {
		// 构建
		AES aes = new AES(Mode.ECB.name(), "pkcs7padding",
				HexUtil.decodeHex("0102030405060708090a0b0c0d0e0f10"));

		// ------------------------------------------------------------------------
		// 加密数据为16进制字符串
		String encryptHex = aes.encryptHex(HexUtil.decodeHex("16c5"));
		// 加密后的Hex
		Assert.equals("25869eb3ff227d9e34b3512d3c3c92ed", encryptHex);

		// 加密数据为16进制字符串
		String encryptHex2 = aes.encryptBase64(HexUtil.decodeHex("16c5"));
		// 加密后的Base64
		Assert.equals("JYaes/8ifZ40s1EtPDyS7Q==", encryptHex2);

		// 解密
		Assert.equals("16c5", HexUtil.encodeHexStr(aes.decrypt("25869eb3ff227d9e34b3512d3c3c92ed")));
		Assert.equals("16c5", HexUtil.encodeHexStr(aes.decrypt(HexUtil.encodeHexStr(Base64.decode("JYaes/8ifZ40s1EtPDyS7Q==")))));
		// ------------------------------------------------------------------------

		// ------------------------------------------------------------------------
		// 加密数据为字符串(UTF-8)
		String encryptStr = aes.encryptHex("16c5");
		// 加密后的Hex
		Assert.equals("79c210d3e304932cf9ea6a9c887c6d7c", encryptStr);

		// 加密数据为字符串(UTF-8)
		String encryptStr2 = aes.encryptBase64("16c5");
		// 加密后的Base64
		Assert.equals("ecIQ0+MEkyz56mqciHxtfA==", encryptStr2);

		// 解密
		Assert.equals("16c5", aes.decryptStr("79c210d3e304932cf9ea6a9c887c6d7c"));
		Assert.equals("16c5", aes.decryptStr(Base64.decode("ecIQ0+MEkyz56mqciHxtfA==")));
		// ------------------------------------------------------------------------
	}

	@Test
	public void aesWithSha1PrngTest() {
		final SecureRandom random = RandomUtil.getSecureRandom("123456".getBytes());
		final SecretKey secretKey = KeyUtil.generateKey("AES", 128, random);

		String content = "12sdfsdfs你好啊！";
		AES aes = new AES(secretKey);
		final String result1 = aes.encryptBase64(content);

		final String decryptStr = aes.decryptStr(result1);
		Assert.equals(content, decryptStr);
	}

	/**
	 * 见：https://blog.csdn.net/weixin_42468911/article/details/114358682
	 */
	@Test
	public void gcmTest() {
		final SecretKey key = KeyUtil.generateKey("AES");
		byte[] iv = RandomUtil.randomBytes(12);

		AES aes = new AES("GCM", "NoPadding",
				key,
				new GCMParameterSpec(128, iv));

		// 原始数据
		String phone = "13534534567";
		// 加密
		byte[] encrypt = aes.encrypt(phone);
		final String decryptStr = aes.decryptStr(encrypt);
		Assert.equals(phone, decryptStr);
	}
}