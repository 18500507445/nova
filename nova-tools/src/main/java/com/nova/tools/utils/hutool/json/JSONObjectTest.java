package com.nova.tools.utils.hutool.json;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.annotation.PropIgnore;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.*;
import com.nova.tools.utils.hutool.json.test.bean.*;
import com.nova.tools.utils.hutool.json.test.bean.report.CaseReport;
import com.nova.tools.utils.hutool.json.test.bean.report.StepReport;
import com.nova.tools.utils.hutool.json.test.bean.report.SuiteReport;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

/**
 * JSONObject单元测试
 *
 * @author: Looly
 */
public class JSONObjectTest {

    @Test
    public void toStringTest() {
        final String str = "{\"code\": 500, \"data\":null}";
        final JSONObject jsonObject = new JSONObject(str);
        Console.log(jsonObject);
        jsonObject.getConfig().setIgnoreNullValue(true);
        Console.log(jsonObject.toStringPretty());
    }

    @Test
    public void toStringTest2() {
        final String str = "{\"test\":\"关于开展2018年度“文明集体”、“文明职工”评选表彰活动的通知\"}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(str);
        Assert.equals(str, json.toString());
    }

    /**
     * 测试JSON中自定义日期格式输出正确性
     */
    @Test
    public void toStringTest3() {
        final JSONObject json = Objects.requireNonNull(JSONUtil.createObj()//
                        .set("dateTime", DateUtil.parse("2019-05-02 22:12:01")))//
                .setDateFormat(DatePattern.NORM_DATE_PATTERN);
        Assert.equals("{\"dateTime\":\"2019-05-02\"}", json.toString());
    }

    @Test
    public void toStringWithDateTest() {
        JSONObject json = JSONUtil.createObj().set("date", DateUtil.parse("2019-05-08 19:18:21"));
        assert json != null;
        Assert.equals("{\"date\":1557314301000}", json.toString());

        json = Objects.requireNonNull(JSONUtil.createObj().set("date", DateUtil.parse("2019-05-08 19:18:21"))).setDateFormat(DatePattern.NORM_DATE_PATTERN);
        Assert.equals("{\"date\":\"2019-05-08\"}", json.toString());
    }


    @Test
    public void putAllTest() {
        final JSONObject json1 = JSONUtil.createObj()
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3")
                .set("d", true);

        final JSONObject json2 = JSONUtil.createObj()
                .set("a", "value21")
                .set("b", "value22");

        // putAll操作会覆盖相同key的值，因此a,b两个key的值改变，c的值不变
        json1.putAll(json2);

        Assert.equals(json1.get("a"), "value21");
        Assert.equals(json1.get("b"), "value22");
        Assert.equals(json1.get("c"), "value3");
    }

    @Test
    public void parseStringTest() {
        final String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
        final JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        Assert.equals(jsonObject.get("a"), "value1");
        Assert.equals(jsonObject.get("b"), "value2");
        Assert.equals(jsonObject.get("c"), "value3");
        Assert.equals(jsonObject.get("d"), true);

        Assert.isTrue(jsonObject.containsKey("e"));
        Assert.equals(jsonObject.get("e"), JSONNull.NULL);
    }

    @Test
    public void parseStringTest2() {
        final String jsonStr = "{\"file_name\":\"RMM20180127009_731.000\",\"error_data\":\"201121151350701001252500000032 18973908335 18973908335 13601893517 201711211700152017112115135420171121 6594000000010100000000000000000000000043190101701001910072 100001100 \",\"error_code\":\"F140\",\"error_info\":\"最早发送时间格式错误，该字段可以为空，当不为空时正确填写格式为“YYYYMMDDHHMISS”\",\"app_name\":\"inter-pre-check\"}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(jsonStr);
        Assert.equals("F140", json.getStr("error_code"));
        Assert.equals("最早发送时间格式错误，该字段可以为空，当不为空时正确填写格式为“YYYYMMDDHHMISS”", json.getStr("error_info"));
    }

    @Test
    public void parseStringTest3() {
        final String jsonStr = "{\"test\":\"体”、“文\"}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(jsonStr);
        Assert.equals("体”、“文", json.getStr("test"));
    }

    @Test
    public void parseStringTest4() {
        final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(jsonStr);
        Assert.equals(new Integer(0), json.getInt("ok"));
        Assert.equals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
    }

    @Test
    public void parseBytesTest() {
        final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(jsonStr.getBytes(StandardCharsets.UTF_8));
        Assert.equals(new Integer(0), json.getInt("ok"));
        Assert.equals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
    }

    @Test
    public void parseReaderTest() {
        final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
        final StringReader stringReader = new StringReader(jsonStr);

        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(stringReader);
        Assert.equals(new Integer(0), json.getInt("ok"));
        Assert.equals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
    }

    @Test
    public void parseInputStreamTest() {
        final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
        final ByteArrayInputStream in = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));

        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(in);
        Assert.equals(new Integer(0), json.getInt("ok"));
        Assert.equals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
    }

    @Test
    public void parseStringWithBomTest() {
        final String jsonStr = FileUtil.readUtf8String("f:/test/jsontest.txt");
        final JSONObject json = new JSONObject(jsonStr);
        final JSONObject json2 = JSONUtil.parseObj(json);
        Console.log(json);
        Console.log(json2);
    }

    @Test
    public void parseStringWithSlashTest() {
        //在5.3.2之前，</div>中的/会被转义，修复此bug的单元测试
        final String jsonStr = "{\"a\":\"<div>aaa</div>\"}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject json = new JSONObject(jsonStr);
        Assert.equals("<div>aaa</div>", json.get("a"));
        Assert.equals(jsonStr, json.toString());
    }

    @Test
    public void toBeanTest() {
        final JSONObject subJson = JSONUtil.createObj().set("value1", "strValue1").set("value2", "234");
        final JSONObject json = JSONUtil.createObj().set("strValue", "strTest").set("intValue", 123)
                // 测试空字符串转对象
                .set("doubleValue", "")
                .set("beanValue", subJson)
                .set("list", JSONUtil.createArray().set("a").set("b")).set("testEnum", "TYPE_A");

        final TestBean bean = json.toBean(TestBean.class);
        Assert.equals("a", bean.getList().get(0));
        Assert.equals("b", bean.getList().get(1));

        Assert.equals("strValue1", bean.getBeanValue().getValue1());
        // BigDecimal转换检查
        Assert.equals(new BigDecimal("234"), bean.getBeanValue().getValue2());
        // 枚举转换检查
        Assert.equals(TestEnum.TYPE_A, bean.getTestEnum());
    }

    @Test
    public void toBeanNullStrTest() {
        final JSONObject json = JSONUtil.createObj()//
                .set("strValue", "null")//
                .set("intValue", 123)//
                // 子对象对应"null"字符串，如果忽略错误，跳过，否则抛出转换异常
                .set("beanValue", "null")//
                .set("list", JSONUtil.createArray().set("a").set("b"));

        final TestBean bean = json.toBean(TestBean.class, true);
        // 当JSON中为字符串"null"时应被当作字符串处理
        Assert.equals("null", bean.getStrValue());
        // 当JSON中为字符串"null"时Bean中的字段类型不匹配应在ignoreError模式下忽略注入
        Assert.isNull(bean.getBeanValue());
    }

    @Test
    public void toBeanTest2() {
        final UserA userA = new UserA();
        userA.setA("A user");
        userA.setName("{\n\t\"body\":{\n\t\t\"loginId\":\"id\",\n\t\t\"password\":\"pwd\"\n\t}\n}");
        userA.setDate(new Date());
        userA.setSqs(CollectionUtil.newArrayList(new Seq("seq1"), new Seq("seq2")));

        final JSONObject json = JSONUtil.parseObj(userA);
        final UserA userA2 = json.toBean(UserA.class);
        // 测试数组
        Assert.equals("seq1", userA2.getSqs().get(0).getSeq());
        // 测试带换行符等特殊字符转换是否成功
        Assert.isTrue(StrUtil.isNotBlank(userA2.getName()));
    }

    @Test
    public void toBeanWithNullTest() {
        final String jsonStr = "{'data':{'userName':'ak','password': null}}";
        final UserWithMap user = JSONUtil.toBean(JSONUtil.parseObj(jsonStr), UserWithMap.class);
        Assert.isTrue(user.getData().containsKey("password"));
    }

    @Test
    public void toBeanTest4() {
        final String json = "{\"data\":{\"b\": \"c\"}}";

        final UserWithMap map = JSONUtil.toBean(json, UserWithMap.class);
        Assert.equals("c", map.getData().get("b"));
    }

    @Test
    public void toBeanTest5() {
        final String readUtf8Str = ResourceUtil.readUtf8Str("suiteReport.json");
        final JSONObject json = JSONUtil.parseObj(readUtf8Str);
        final SuiteReport bean = json.toBean(SuiteReport.class);

        // 第一层
        final List<CaseReport> caseReports = bean.getCaseReports();
        final CaseReport caseReport = caseReports.get(0);
        Assert.notNull(caseReport);

        // 第二层
        final List<StepReport> stepReports = caseReports.get(0).getStepReports();
        final StepReport stepReport = stepReports.get(0);
        Assert.notNull(stepReport);
    }

    /**
     * 在JSON转Bean过程中，Bean中字段如果为父类定义的泛型类型，则应正确转换，此方法用于测试这类情况
     */
    @Test
    public void toBeanTest6() {
        final JSONObject json = JSONUtil.createObj()
                .set("targetUrl", "http://test.com")
                .set("success", "true")
                .set("result", JSONUtil.createObj()
                        .set("token", "tokenTest")
                        .set("userId", "测试用户1"));

        final TokenAuthWarp2 bean = json.toBean(TokenAuthWarp2.class);
        Assert.equals("http://test.com", bean.getTargetUrl());
        Assert.equals("true", bean.getSuccess());

        final TokenAuthResponse result = bean.getResult();
        Assert.notNull(result);
        Assert.equals("tokenTest", result.getToken());
        Assert.equals("测试用户1", result.getUserId());
    }

    /**
     * 泛型对象中的泛型参数如果未定义具体类型，按照JSON处理<br>
     * 此处用于测试获取泛型类型实际类型错误导致的空指针问题
     */
    @Test
    public void toBeanTest7() {
        final String jsonStr = " {\"result\":{\"phone\":\"15926297342\",\"appKey\":\"e1ie12e1ewsdqw1\"," +
                "\"secret\":\"dsadadqwdqs121d1e2\",\"message\":\"hello world\"},\"code\":100,\"" +
                "message\":\"validate message\"}";
        final ResultDto<?> dto = JSONUtil.toBean(jsonStr, ResultDto.class);
        Assert.equals("validate message", dto.getMessage());
    }

    @Test
    public void parseBeanTest() {
        final UserA userA = new UserA();
        userA.setName("nameTest");
        userA.setDate(new Date());
        userA.setSqs(CollectionUtil.newArrayList(new Seq(null), new Seq("seq2")));

        final JSONObject json = JSONUtil.parseObj(userA, false);

        Assert.isTrue(json.containsKey("a"));
        Assert.isTrue(json.getJSONArray("sqs").getJSONObject(0).containsKey("seq"));
    }

    @Test
    public void parseBeanTest2() {
        final TestBean bean = new TestBean();
        bean.setDoubleValue(111.1);
        bean.setIntValue(123);
        bean.setList(CollUtil.newArrayList("a", "b", "c"));
        bean.setStrValue("strTest");
        bean.setTestEnum(TestEnum.TYPE_B);

        final JSONObject json = JSONUtil.parseObj(bean, false);
        // 枚举转换检查
        Assert.equals("TYPE_B", json.get("testEnum"));

        final TestBean bean2 = json.toBean(TestBean.class);
        Assert.equals(bean.toString(), bean2.toString());
    }

    @Test
    public void parseBeanTest3() {
        final JSONObject json = JSONUtil.createObj()
                .set("code", 22)
                .set("data", "{\"jobId\": \"abc\", \"videoUrl\": \"http://a.com/a.mp4\"}");

        final JSONBean bean = json.toBean(JSONBean.class);
        Assert.equals(22, bean.getCode());
        Assert.equals("abc", bean.getData().getObj("jobId"));
        Assert.equals("http://a.com/a.mp4", bean.getData().getObj("videoUrl"));
    }

    @Test
    public void beanTransTest() {
        final UserA userA = new UserA();
        userA.setA("A user");
        userA.setName("nameTest");
        userA.setDate(new Date());

        final JSONObject userAJson = JSONUtil.parseObj(userA);
        final UserB userB = JSONUtil.toBean(userAJson, UserB.class);

        Assert.equals(userA.getName(), userB.getName());
        Assert.equals(userA.getDate(), userB.getDate());
    }

    @Test
    public void beanTransTest2() {
        final UserA userA = new UserA();
        userA.setA("A user");
        userA.setName("nameTest");
        userA.setDate(DateUtil.parse("2018-10-25"));

        final JSONObject userAJson = JSONUtil.parseObj(userA);
        // 自定义日期格式
        userAJson.setDateFormat("yyyy-MM-dd");

        final UserA bean = JSONUtil.toBean(userAJson.toString(), UserA.class);
        Assert.equals(DateUtil.parse("2018-10-25"), bean.getDate());
    }

    @Test
    public void beanTransTest3() {
        final JSONObject userAJson = JSONUtil.createObj()
                .set("a", "AValue")
                .set("name", "nameValue")
                .set("date", "08:00:00");
        final UserA bean = JSONUtil.toBean(userAJson.toString(), UserA.class);
        Assert.equals(DateUtil.today() + " 08:00:00", DateUtil.date(bean.getDate()).toString());
    }

    @Test
    public void parseFromBeanTest() {
        final UserA userA = new UserA();
        userA.setA(null);
        userA.setName("nameTest");
        userA.setDate(new Date());

        final JSONObject userAJson = JSONUtil.parseObj(userA);
        Assert.isFalse(userAJson.containsKey("a"));

        final JSONObject userAJsonWithNullValue = JSONUtil.parseObj(userA, false);
        Assert.isTrue(userAJsonWithNullValue.containsKey("a"));
        Assert.isTrue(userAJsonWithNullValue.containsKey("sqs"));
    }

    @Test
    public void specialCharTest() {
        final String json = "{\"pattern\": \"[abc]\b\u2001\", \"pattern2Json\": {\"patternText\": \"[ab]\\b\"}}";
        final JSONObject obj = JSONUtil.parseObj(json);
        Assert.equals("[abc]\\b\\u2001", obj.getStrEscaped("pattern"));
        Assert.equals("{\"patternText\":\"[ab]\\b\"}", obj.getStrEscaped("pattern2Json"));
    }

    @Test
    public void getStrTest() {
        final String json = "{\"name\": \"yyb\\nbbb\"}";
        final JSONObject jsonObject = JSONUtil.parseObj(json);

        // 没有转义按照默认规则显示
        Assert.equals("yyb\nbbb", jsonObject.getStr("name"));
        // 转义按照字符串显示
        Assert.equals("yyb\\nbbb", jsonObject.getStrEscaped("name"));

        final String bbb = jsonObject.getStr("bbb", "defaultBBB");
        Assert.equals("defaultBBB", bbb);
    }

    @Test
    public void aliasTest() {
        final BeanWithAlias beanWithAlias = new BeanWithAlias();
        beanWithAlias.setValue1("张三");
        beanWithAlias.setValue2(35);

        final JSONObject jsonObject = JSONUtil.parseObj(beanWithAlias);
        Assert.equals("张三", jsonObject.getStr("name"));
        Assert.equals(new Integer(35), jsonObject.getInt("age"));

        final JSONObject json = JSONUtil.createObj()
                .set("name", "张三")
                .set("age", 35);
        final BeanWithAlias bean = JSONUtil.toBean(Objects.requireNonNull(json).toString(), BeanWithAlias.class);
        Assert.equals("张三", bean.getValue1());
        Assert.equals(new Integer(35), bean.getValue2());
    }

    @Test
    public void setDateFormatTest() {
        final JSONConfig jsonConfig = JSONConfig.create();
        jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        final JSONObject json = new JSONObject(jsonConfig);
        json.append("date", DateUtil.parse("2020-06-05 11:16:11"));
        json.append("bbb", "222");
        json.append("aaa", "123");
        Assert.equals("{\"date\":[\"2020-06-05 11:16:11\"],\"bbb\":[\"222\"],\"aaa\":[\"123\"]}", json.toString());
    }

    @Test
    public void setDateFormatTest2() {
        final JSONConfig jsonConfig = JSONConfig.create();
        jsonConfig.setDateFormat("yyyy#MM#dd");

        final Date date = DateUtil.parse("2020-06-05 11:16:11");
        final JSONObject json = new JSONObject(jsonConfig);
        json.set("date", date);
        json.set("bbb", "222");
        json.set("aaa", "123");

        final String jsonStr = "{\"date\":\"2020#06#05\",\"bbb\":\"222\",\"aaa\":\"123\"}";

        Assert.equals(jsonStr, json.toString());

        // 解析测试
        final JSONObject parse = JSONUtil.parseObj(jsonStr, jsonConfig);
        Assert.equals(DateUtil.beginOfDay(date), parse.getDate("date"));
    }

    @Test
    public void setCustomDateFormatTest() {
        final JSONConfig jsonConfig = JSONConfig.create();
        jsonConfig.setDateFormat("#sss");

        final Date date = DateUtil.parse("2020-06-05 11:16:11");
        final JSONObject json = new JSONObject(jsonConfig);
        json.set("date", date);
        json.set("bbb", "222");
        json.set("aaa", "123");

        final String jsonStr = "{\"date\":1591326971,\"bbb\":\"222\",\"aaa\":\"123\"}";

        Assert.equals(jsonStr, json.toString());

        // 解析测试
        final JSONObject parse = JSONUtil.parseObj(jsonStr, jsonConfig);
        Assert.equals(date, parse.getDate("date"));
    }

    @Test
    public void getTimestampTest() {
        final String timeStr = "1970-01-01 00:00:00";
        final JSONObject jsonObject = JSONUtil.createObj().set("time", timeStr);
        final Timestamp time = jsonObject.get("time", Timestamp.class);
        Assert.equals("1970-01-01 00:00:00.0", time.toString());
    }

    public enum TestEnum {
        TYPE_A, TYPE_B
    }

    /**
     * 测试Bean
     *
     * @author: Looly
     */
    @Data
    public static class TestBean {
        private String strValue;
        private int intValue;
        private Double doubleValue;
        private SubBean beanValue;
        private List<String> list;
        private TestEnum testEnum;
    }

    /**
     * 测试子Bean
     *
     * @author: Looly
     */
    @Data
    public static class SubBean {
        private String value1;
        private BigDecimal value2;
    }

    @Data
    public static class BeanWithAlias {
        @Alias("name")
        private String value1;
        @Alias("age")
        private Integer value2;
    }

    @Test
    public void parseBeanSameNameTest() {
        final SameNameBean sameNameBean = new SameNameBean();
        final JSONObject parse = JSONUtil.parseObj(sameNameBean);
        Assert.equals("123", parse.getStr("username"));
        Assert.equals("abc", parse.getStr("userName"));

        // 测试ignore注解是否有效
        Assert.isNull(parse.getStr("fieldToIgnore"));
    }

    /**
     * 测试子Bean
     *
     * @author: Looly
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static class SameNameBean {
        private final String username = "123";
        private final String userName = "abc";

        public String getUsername() {
            return username;
        }

        @PropIgnore
        private final String fieldToIgnore = "sfdsdads";

        public String getUserName() {
            return userName;
        }

        public String getFieldToIgnore() {
            return this.fieldToIgnore;
        }
    }

    @Test
    public void setEntryTest() {
        final HashMap<String, String> of = MapUtil.of("test", "testValue");
        final Set<Map.Entry<String, String>> entries = of.entrySet();
        final Map.Entry<String, String> next = entries.iterator().next();

        final JSONObject jsonObject = JSONUtil.parseObj(next);
        Assert.equals("{\"test\":\"testValue\"}", jsonObject.toString());
    }

    @Test
    public void createJSONObjectTest() {
        // 集合类不支持转为JSONObject
        new JSONObject(new JSONArray(), JSONConfig.create());
    }

    @Test
    public void floatTest() {
        final Map<String, Object> map = new HashMap<>();
        map.put("c", 2.0F);

        final String s = JSONUtil.toJsonStr(map);
        Assert.equals("{\"c\":2}", s);
    }

    @Test
    public void accumulateTest() {
        final JSONObject jsonObject = JSONUtil.createObj().accumulate("key1", "value1");
        Assert.equals("{\"key1\":\"value1\"}", jsonObject.toString());

        jsonObject.accumulate("key1", "value2");
        Assert.equals("{\"key1\":[\"value1\",\"value2\"]}", jsonObject.toString());

        jsonObject.accumulate("key1", "value3");
        Assert.equals("{\"key1\":[\"value1\",\"value2\",\"value3\"]}", jsonObject.toString());
    }

    @Test
    public void putByPathTest() {
        final JSONObject json = new JSONObject();
        json.putByPath("aa.bb", "BB");
        Assert.equals("{\"aa\":{\"bb\":\"BB\"}}", json.toString());
    }


    @Test
    public void bigDecimalTest() {
        final String jsonStr = "{\"orderId\":\"1704747698891333662002277\"}";
        final BigDecimalBean bigDecimalBean = JSONUtil.toBean(jsonStr, BigDecimalBean.class);
        Assert.equals("{\"orderId\":1704747698891333662002277}", JSONUtil.toJsonStr(bigDecimalBean));
    }

    @Data
    static
    class BigDecimalBean {
        private BigDecimal orderId;
    }

    @Test
    public void filterIncludeTest() {
        final JSONObject json1 = JSONUtil.createObj(JSONConfig.create())
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3")
                .set("d", true);

        final String s = json1.toJSONString(0, (pair) -> pair.getKey().equals("b"));
        Assert.equals("{\"b\":\"value2\"}", s);
    }

    @Test
    public void filterExcludeTest() {
        final JSONObject json1 = JSONUtil.createObj(JSONConfig.create())
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3")
                .set("d", true);

        final String s = json1.toJSONString(0, (pair) -> false == pair.getKey().equals("b"));
        Assert.equals("{\"a\":\"value1\",\"c\":\"value3\",\"d\":true}", s);
    }

    @Test
    public void editTest() {
        final JSONObject json1 = JSONUtil.createObj(JSONConfig.create())
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3")
                .set("d", true);

        final String s = json1.toJSONString(0, (pair) -> {
            if ("b".equals(pair.getKey())) {
                // 修改值为新值
                pair.setValue(pair.getValue() + "_edit");
                return true;
            }
            // 除了"b"，其他都去掉
            return false;
        });
        Assert.equals("{\"b\":\"value2_edit\"}", s);
    }

    @Test
    public void toUnderLineCaseTest() {
        final JSONObject json1 = JSONUtil.createObj(JSONConfig.create())
                .set("aKey", "value1")
                .set("bJob", "value2")
                .set("cGood", "value3")
                .set("d", true);

        final String s = json1.toJSONString(0, (pair) -> {
            pair.setKey(StrUtil.toUnderlineCase(pair.getKey().toString()));
            return true;
        });
        Assert.equals("{\"a_key\":\"value1\",\"b_job\":\"value2\",\"c_good\":\"value3\",\"d\":true}", s);
    }

    @Test
    public void nullToEmptyTest() {
        final JSONObject json1 = JSONUtil.createObj(JSONConfig.create().setIgnoreNullValue(false))
                .set("a", null)
                .set("b", "value2");

        final String s = json1.toJSONString(0, (pair) -> {
            pair.setValue(ObjectUtil.defaultIfNull(pair.getValue(), StrUtil.EMPTY));
            return true;
        });
        Assert.equals("{\"a\":\"\",\"b\":\"value2\"}", s);
    }

    @Test
    public void parseFilterTest() {
        final String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject jsonObject = new JSONObject(jsonStr, null, (pair) -> "b".equals(pair.getKey()));
        Assert.equals(1, jsonObject.size());
        Assert.equals("value2", jsonObject.get("b"));
    }

    @Test
    public void parseFilterEditTest() {
        final String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject jsonObject = new JSONObject(jsonStr, null, (pair) -> {
            if ("b".equals(pair.getKey())) {
                pair.setValue(pair.getValue() + "_edit");
            }
            return true;
        });
        Assert.equals("value2_edit", jsonObject.get("b"));
    }
}
