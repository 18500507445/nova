package com.nova.tools.utils.hutool.json;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.*;
import cn.hutool.core.lang.Assert;
import com.nova.tools.utils.hutool.json.test.bean.Price;
import com.nova.tools.utils.hutool.json.test.bean.UserA;
import com.nova.tools.utils.hutool.json.test.bean.UserC;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

public class JSONUtilTest {

	/**
	 * 出现语法错误时报错，检查解析\x字符时是否会导致死循环异常
	 */
	@Test
	public void parseTest() {
		final JSONArray jsonArray = JSONUtil.parseArray("[{\"a\":\"a\\x]");
		Console.log(jsonArray);
	}

	/**
	 * 数字解析为JSONArray报错
	 */
	@Test
	public void parseNumberTest() {
		final JSONArray json = JSONUtil.parseArray(123L);
		Assert.notNull(json);
	}

	/**
	 * 数字解析为JSONObject忽略
	 */
	@Test
	public void parseNumberTest2() {
		final JSONObject json = JSONUtil.parseObj(123L);
		Assert.equals(new JSONObject(), json);
	}

	@Test
	public void toJsonStrTest() {
		final UserA a1 = new UserA();
		a1.setA("aaaa");
		a1.setDate(DateUtil.date());
		a1.setName("AAAAName");
		final UserA a2 = new UserA();
		a2.setA("aaaa222");
		a2.setDate(DateUtil.date());
		a2.setName("AAAA222Name");

		final ArrayList<UserA> list = CollectionUtil.newArrayList(a1, a2);
		final HashMap<String, Object> map = MapUtil.newHashMap();
		map.put("total", 13);
		map.put("rows", list);

		final String str = JSONUtil.toJsonPrettyStr(map);
		JSONUtil.parse(str);
		Assert.notNull(str);
	}

	@Test
	public void toJsonStrTest2() {
		final Map<String, Object> model = new HashMap<>();
		model.put("mobile", "17610836523");
		model.put("type", 1);

		final Map<String, Object> data = new HashMap<>();
		data.put("model", model);
		data.put("model2", model);

		final JSONObject jsonObject = JSONUtil.parseObj(data);

		Assert.isTrue(jsonObject.containsKey("model"));
		Assert.equals(1, jsonObject.getJSONObject("model").getInt("type").intValue());
		Assert.equals("17610836523", jsonObject.getJSONObject("model").getStr("mobile"));
		// Assert.equals("{\"model\":{\"type\":1,\"mobile\":\"17610836523\"}}", jsonObject.toString());
	}

	@Test
	public void toJsonStrTest3() {
		// 验证某个字段为JSON字符串时转义是否规范
		final JSONObject object = new JSONObject(true);
		object.set("name", "123123");
		object.set("value", "\\");
		object.set("value2", "</");

		final HashMap<String, String> map = MapUtil.newHashMap();
		map.put("user", object.toString());

		final JSONObject json = JSONUtil.parseObj(map);
		Assert.equals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"</\"}", json.get("user"));
		Assert.equals("{\"user\":\"{\\\"name\\\":\\\"123123\\\",\\\"value\\\":\\\"\\\\\\\\\\\",\\\"value2\\\":\\\"</\\\"}\"}", json.toString());

		final JSONObject json2 = JSONUtil.parseObj(json.toString());
		Assert.equals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"</\"}", json2.get("user"));
	}

	@Test
	public void toJsonStrFromSortedTest() {
		final SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
			private static final long serialVersionUID = 1L;

			{
			put("attributes", "a");
			put("b", "b");
			put("c", "c");
		}};

		Assert.equals("{\"attributes\":\"a\",\"b\":\"b\",\"c\":\"c\"}", JSONUtil.toJsonStr(sortedMap));
	}

	/**
	 * 泛型多层嵌套测试
	 */
	@Test
	public void toBeanTest() {
		final String json = "{\"ADT\":[[{\"BookingCode\":[\"N\",\"N\"]}]]}";

		final Price price = JSONUtil.toBean(json, Price.class);
		Assert.equals("N", price.getADT().get(0).get(0).getBookingCode().get(0));
	}

	@Test
	public void toBeanTest2() {
		// 测试JSONObject转为Bean中字符串字段的情况
		final String json = "{\"id\":123,\"name\":\"张三\",\"prop\":{\"gender\":\"男\", \"age\":18}}";
		final UserC user = JSONUtil.toBean(json, UserC.class);
		Assert.notNull(user.getProp());
		final String prop = user.getProp();
		final JSONObject propJson = JSONUtil.parseObj(prop);
		Assert.equals("男", propJson.getStr("gender"));
		Assert.equals(18, propJson.getInt("age").intValue());
		// Assert.equals("{\"age\":18,\"gender\":\"男\"}", user.getProp());
	}

	@Test
	public void getStrTest() {
		final String html = "{\"name\":\"Something must have been changed since you leave\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(html);
		Assert.equals("Something must have been changed since you leave", jsonObject.getStr("name"));
	}

	@Test
	public void getStrTest2() {
		final String html = "{\"name\":\"Something\\u00a0must have been changed since you leave\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(html);
		Assert.equals("Something\\u00a0must\\u00a0have\\u00a0been\\u00a0changed\\u00a0since\\u00a0you\\u00a0leave", jsonObject.getStrEscaped("name"));
	}

	@Test
	public void parseFromXmlTest() {
		final String s = "<sfzh>640102197312070614</sfzh><sfz>640102197312070614X</sfz><name>aa</name><gender>1</gender>";
		final JSONObject json = JSONUtil.parseFromXml(s);
		Assert.equals(640102197312070614L, json.get("sfzh"));
		Assert.equals("640102197312070614X", json.get("sfz"));
		Assert.equals("aa", json.get("name"));
		Assert.equals(1, json.get("gender"));
	}

	@Test
	public void doubleTest() {
		final String json = "{\"test\": 12.00}";
		final JSONObject jsonObject = JSONUtil.parseObj(json);
		//noinspection BigDecimalMethodWithoutRoundingCalled
		Assert.equals("12.00", jsonObject.getBigDecimal("test").setScale(2).toString());
	}

	@Test
	public void customValueTest() {
		final JSONObject jsonObject = JSONUtil.createObj()
		.set("test2", (JSONString) () -> NumberUtil.decimalFormat("#.0", 12.00D));

		Assert.equals("{\"test2\":12.0}", jsonObject.toString());
	}

	@Test
	public void setStripTrailingZerosTest() {
		// 默认去除多余的0
		final JSONObject jsonObjectDefault = JSONUtil.createObj()
				.set("test2", 12.00D);
		Assert.equals("{\"test2\":12}", jsonObjectDefault.toString());

		// 不去除多余的0
		final JSONObject jsonObject = JSONUtil.createObj(JSONConfig.create().setStripTrailingZeros(false))
				.set("test2", 12.00D);
		Assert.equals("{\"test2\":12.0}", jsonObject.toString());

		// 去除多余的0
		jsonObject.getConfig().setStripTrailingZeros(true);
		Assert.equals("{\"test2\":12}", jsonObject.toString());
	}

	@Test
	public void parseObjTest() {
		// 测试转义
		final JSONObject jsonObject = JSONUtil.parseObj("{\n" +
				"    \"test\": \"\\\\地库地库\",\n" +
				"}");

		Assert.equals("\\地库地库", jsonObject.getObj("test"));
	}

	@Test
	public void sqlExceptionTest(){
		//https://github.com/dromara/hutool/issues/1399
		// SQLException实现了Iterable接口，默认是遍历之，会栈溢出，修正后只返回string
		final JSONObject set = JSONUtil.createObj().set("test", new SQLException("test"));
		Assert.equals("{\"test\":\"java.sql.SQLException: test\"}", set.toString());
	}

	@Test
	public void parseBigNumberTest(){
		// 科学计数法使用BigDecimal处理，默认输出非科学计数形式
		final String str = "{\"test\":100000054128897953e4}";
		Assert.equals("{\"test\":1000000541288979530000}", JSONUtil.parseObj(str).toString());
	}

	@Test
	public void toXmlTest(){
		final JSONObject obj = JSONUtil.createObj();
		obj.set("key1", "v1")
				.set("key2", ListUtil.of("a", "b", "c"));
		final String xmlStr = JSONUtil.toXmlStr(obj);
		Assert.equals("<key1>v1</key1><key2>a</key2><key2>b</key2><key2>c</key2>", xmlStr);
	}

	@Test
	public void duplicateKeyFalseTest(){
		final String str = "{id:123, name:\"张三\", name:\"李四\"}";

		final JSONObject jsonObject = JSONUtil.parseObj(str, JSONConfig.create().setCheckDuplicate(false));
		Assert.equals("{\"id\":123,\"name\":\"李四\"}", jsonObject.toString());
	}

	@Test
	public void duplicateKeyTrueTest(){
		final String str = "{id:123, name:\"张三\", name:\"李四\"}";

		final JSONObject jsonObject = JSONUtil.parseObj(str, JSONConfig.create().setCheckDuplicate(true));
		Assert.equals("{\"id\":123,\"name\":\"李四\"}", jsonObject.toString());
	}
}
