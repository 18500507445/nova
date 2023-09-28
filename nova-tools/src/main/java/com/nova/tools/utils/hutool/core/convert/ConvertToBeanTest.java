package com.nova.tools.utils.hutool.core.convert;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.CaseInsensitiveMap;
import com.nova.tools.utils.hutool.core.bean.BeanUtilTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类型转换工具单元测试<br>
 * 转换为数组
 *
 * @author Looly
 */
public class ConvertToBeanTest {

    @Test
    public void beanToMapTest() {
        BeanUtilTest.SubPerson person = new BeanUtilTest.SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        Map<?, ?> map = Convert.convert(Map.class, person);
        Assert.equals(map.get("name"), "测试A11");
        Assert.equals(map.get("age"), 14);
        Assert.equals("11213232", map.get("openid"));
    }

    @Test
    public void beanToMapTest2() {
        BeanUtilTest.SubPerson person = new BeanUtilTest.SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        Map<String, String> map = Convert.toMap(String.class, String.class, person);
        Assert.equals("测试A11", map.get("name"));
        Assert.equals("14", map.get("age"));
        Assert.equals("11213232", map.get("openid"));

        final LinkedHashMap<String, String> map2 = Convert.convert(
                new TypeReference<LinkedHashMap<String, String>>() {
                }, person);
        Assert.equals("测试A11", map2.get("name"));
        Assert.equals("14", map2.get("age"));
        Assert.equals("11213232", map2.get("openid"));
    }

    @Test
    public void mapToMapTest() {
        LinkedHashMap<String, Integer> map1 = new LinkedHashMap<>();
        map1.put("key1", 1);
        map1.put("key2", 2);
        map1.put("key3", 3);
        map1.put("key4", 4);

        Map<String, String> map2 = Convert.toMap(String.class, String.class, map1);

        Assert.equals("1", map2.get("key1"));
        Assert.equals("2", map2.get("key2"));
        Assert.equals("3", map2.get("key3"));
        Assert.equals("4", map2.get("key4"));
    }

    @Test
    public void mapToMapWithSelfTypeTest() {
        CaseInsensitiveMap<String, Integer> caseInsensitiveMap = new CaseInsensitiveMap<>();
        caseInsensitiveMap.put("jerry", 1);
        caseInsensitiveMap.put("Jerry", 2);
        caseInsensitiveMap.put("tom", 3);

        Map<String, String> map = Convert.toMap(String.class, String.class, caseInsensitiveMap);
        Assert.equals("2", map.get("jerry"));
        Assert.equals("2", map.get("Jerry"));
        Assert.equals("3", map.get("tom"));
    }

    @Test
    public void beanToSpecifyMapTest() {
        BeanUtilTest.SubPerson person = new BeanUtilTest.SubPerson();
        person.setAge(14);
        person.setOpenid("11213232");
        person.setName("测试A11");
        person.setSubName("sub名字");

        Map<String, String> map = Convert.toMap(LinkedHashMap.class, String.class, String.class, person);
        Assert.equals("测试A11", map.get("name"));
        Assert.equals("14", map.get("age"));
        Assert.equals("11213232", map.get("openid"));
    }

    @Test
    public void mapToBeanTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", "88dc4b28-91b1-4a1a-bab5-444b795c7ecd");
        map.put("age", 14);
        map.put("openid", "11213232");
        map.put("name", "测试A11");
        map.put("subName", "sub名字");

        BeanUtilTest.SubPerson subPerson = Convert.convert(BeanUtilTest.SubPerson.class, map);
        Assert.equals("88dc4b28-91b1-4a1a-bab5-444b795c7ecd", subPerson.getId().toString());
        Assert.equals(14, subPerson.getAge());
        Assert.equals("11213232", subPerson.getOpenid());
        Assert.equals("测试A11", subPerson.getName());
        Assert.equals("11213232", subPerson.getOpenid());
    }

    @Test
    public void nullStrToBeanTest() {
        String nullStr = "null";
        final BeanUtilTest.SubPerson subPerson = Convert.convertQuietly(BeanUtilTest.SubPerson.class, nullStr);
        Assert.isNull(subPerson);
    }
}
