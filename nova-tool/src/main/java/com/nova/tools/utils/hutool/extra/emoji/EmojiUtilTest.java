package com.nova.tools.utils.hutool.extra.emoji;

import cn.hutool.extra.emoji.EmojiUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class EmojiUtilTest {
	
	@Test
	public void toUnicodeTest() {
		String emoji = EmojiUtil.toUnicode(":smile:");
		Assert.equals("😄", emoji);
	}
	
	@Test
	public void toAliasTest() {
		String alias = EmojiUtil.toAlias("😄");
		Assert.equals(":smile:", alias);
	}
	
	@Test
	public void containsEmojiTest() {
		boolean containsEmoji = EmojiUtil.containsEmoji("测试一下是否包含EMOJ:😄");
		Assert.isTrue(containsEmoji);
		boolean notContainsEmoji = EmojiUtil.containsEmoji("不包含EMOJ:^_^");
		Assert.isFalse(notContainsEmoji);

	}
}
