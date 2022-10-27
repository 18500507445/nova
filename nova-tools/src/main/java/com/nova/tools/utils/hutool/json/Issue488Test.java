package com.nova.tools.utils.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue488Test {

	@Test
	public void toBeanTest() {
		String jsonStr = ResourceUtil.readUtf8Str("issue488.json");
		
		ResultSuccess<List<EmailAddress>> result = JSONUtil.toBean(jsonStr,
				new TypeReference<ResultSuccess<List<EmailAddress>>>() {}, false);
		
		Assert.equals("https://graph.microsoft.com/beta/$metadata#Collection(microsoft.graph.emailAddress)", result.getContext());
		
		List<EmailAddress> adds = result.getValue();
		Assert.equals("会议室101", adds.get(0).getName());
		Assert.equals("MeetingRoom101@abc.com", adds.get(0).getAddress());
		Assert.equals("会议室102", adds.get(1).getName());
		Assert.equals("MeetingRoom102@abc.com", adds.get(1).getAddress());
		Assert.equals("会议室103", adds.get(2).getName());
		Assert.equals("MeetingRoom103@abc.com", adds.get(2).getAddress());
		Assert.equals("会议室219", adds.get(3).getName());
		Assert.equals("MeetingRoom219@abc.com", adds.get(3).getAddress());
	}

	@Test
	public void toCollctionBeanTest() {
		String jsonStr = ResourceUtil.readUtf8Str("issue488Array.json");

		List<ResultSuccess<List<EmailAddress>>> resultList = JSONUtil.toBean(jsonStr,
				new TypeReference<List<ResultSuccess<List<EmailAddress>>>>() {}, false);

		ResultSuccess<List<EmailAddress>> result = resultList.get(0);

		Assert.equals("https://graph.microsoft.com/beta/$metadata#Collection(microsoft.graph.emailAddress)", result.getContext());

		List<EmailAddress> adds = result.getValue();
		Assert.equals("会议室101", adds.get(0).getName());
		Assert.equals("MeetingRoom101@abc.com", adds.get(0).getAddress());
		Assert.equals("会议室102", adds.get(1).getName());
		Assert.equals("MeetingRoom102@abc.com", adds.get(1).getAddress());
		Assert.equals("会议室103", adds.get(2).getName());
		Assert.equals("MeetingRoom103@abc.com", adds.get(2).getAddress());
		Assert.equals("会议室219", adds.get(3).getName());
		Assert.equals("MeetingRoom219@abc.com", adds.get(3).getAddress());
	}

	@Data
	public static class ResultSuccess<T> {
		private String context;
		private T value;
	}

	@Data
	public static class EmailAddress {
		private String name;
		private String address;
	}
}
