package com.nova.tools.utils.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class IssueI3EGJP {

	@Test
	public void hutoolMapToBean() {
		JSONObject paramJson = new JSONObject();
		paramJson.set("is_booleana", "1");
		paramJson.set("is_booleanb", true);
		ConvertDO convertDO = BeanUtil.toBean(paramJson, ConvertDO.class);

		Assert.isTrue(convertDO.isBooleana());
		Assert.isTrue(convertDO.getIsBooleanb());
	}

	@Data
	public static class ConvertDO {
		private boolean isBooleana;
		private Boolean isBooleanb;
	}
}
