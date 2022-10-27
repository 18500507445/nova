package com.nova.tools.utils.hutool.crypto.symmetric.fpe;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.fpe.FPE;
import org.bouncycastle.crypto.util.BasicAlphabetMapper;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class FPETest {

	@Test
	public void ff1Test(){
		// 映射字符表，规定了明文和密文的字符范围
		BasicAlphabetMapper numberMapper = new BasicAlphabetMapper("A0123456789");
		// 初始化 aes 密钥
		byte[] keyBytes = RandomUtil.randomBytes(16);

		final FPE fpe = new FPE(FPE.FPEMode.FF1, keyBytes, numberMapper, null);

		// 原始数据
		String phone = RandomUtil.randomString("A0123456789", 13);
		final String encrypt = fpe.encrypt(phone);
		// 加密后与原密文长度一致
		Assert.equals(phone.length(), encrypt.length());

		final String decrypt = fpe.decrypt(encrypt);
		Assert.equals(phone, decrypt);
	}

	@Test
	public void ff3Test(){
		// 映射字符表，规定了明文和密文的字符范围
		BasicAlphabetMapper numberMapper = new BasicAlphabetMapper("A0123456789");
		// 初始化 aes 密钥
		byte[] keyBytes = RandomUtil.randomBytes(16);

		final FPE fpe = new FPE(FPE.FPEMode.FF3_1, keyBytes, numberMapper, null);

		// 原始数据
		String phone = RandomUtil.randomString("A0123456789", 13);
		final String encrypt = fpe.encrypt(phone);
		// 加密后与原密文长度一致
		Assert.equals(phone.length(), encrypt.length());

		final String decrypt = fpe.decrypt(encrypt);
		Assert.equals(phone, decrypt);
	}
}
