package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.JNDIUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * {@link JNDIUtil} 它是为JAVA应用程序提供命名和目录访问服务的API
 * （Application Programing Interface，应用程序编程接口）。
 *
 * @author
 */
public class JNDIUtilTest {

	@Test
	@Ignore
	public void getDnsTest() throws NamingException {
		final Attributes attributes = JNDIUtil.getAttributes("dns:paypal.com", "TXT");
		for (Attribute attribute: new EnumerationIter<>(attributes.getAll())){
			Console.log(attribute.get());
		}
	}
}
