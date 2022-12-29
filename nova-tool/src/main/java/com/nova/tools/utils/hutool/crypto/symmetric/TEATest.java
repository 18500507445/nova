package com.nova.tools.utils.hutool.crypto.symmetric;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.crypto.symmetric.XXTEA;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * TEA（Tiny Encryption Algorithm）和 XTEA算法单元测试
 */
public class TEATest {

	@Test
	public void teaTest() {
		String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final SymmetricCrypto tea = new SymmetricCrypto("TEA", "MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assert.equals(data, decryptStr);
	}

	@Test
	public void xteaTest() {
		String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final SymmetricCrypto tea = new SymmetricCrypto("XTEA", "MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assert.equals(data, decryptStr);
	}

	@Test
	public void xxteaTest() {
		String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final XXTEA tea = new XXTEA("MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assert.equals(data, decryptStr);
	}
}
