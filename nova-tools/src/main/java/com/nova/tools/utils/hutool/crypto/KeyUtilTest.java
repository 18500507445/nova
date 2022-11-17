package com.nova.tools.utils.hutool.crypto;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.KeyUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtilTest {

	/**
	 * 测试关闭BouncyCastle支持时是否会正常抛出异常，即关闭是否有效
	 */
	@Test
	@Ignore
	public void generateKeyPairTest() {
		GlobalBouncyCastleProvider.setUseBouncyCastle(false);
		KeyPair pair = KeyUtil.generateKeyPair("SM2");
		Assert.notNull(pair);
	}

	@Test
	public void getRSAPublicKeyTest(){
		final KeyPair keyPair = KeyUtil.generateKeyPair("RSA");
		final PrivateKey aPrivate = keyPair.getPrivate();
		final PublicKey rsaPublicKey = KeyUtil.getRSAPublicKey(aPrivate);
		Assert.equals(rsaPublicKey, keyPair.getPublic());
	}

	/**
	 * 测试EC和ECIES算法生成的KEY是一致的
	 */
	@Test
	public void generateECIESKeyTest(){
		final KeyPair ecies = KeyUtil.generateKeyPair("ECIES");
		Assert.notNull(ecies.getPrivate());
		Assert.notNull(ecies.getPublic());

		byte[] privateKeyBytes = ecies.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("EC", privateKeyBytes);
		Assert.equals(ecies.getPrivate(), privateKey);
	}

	@Test
	public void generateDHTest(){
		final KeyPair dh = KeyUtil.generateKeyPair("DH");
		Assert.notNull(dh.getPrivate());
		Assert.notNull(dh.getPublic());

		byte[] privateKeyBytes = dh.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("DH", privateKeyBytes);
		Assert.equals(dh.getPrivate(), privateKey);
	}

	@Test
	public void generateSm4KeyTest(){
		// https://github.com/dromara/hutool/issues/2150
		Assert.equals(16, KeyUtil.generateKey("sm4").getEncoded().length);
		Assert.equals(32, KeyUtil.generateKey("sm4", 256).getEncoded().length);
	}
}
