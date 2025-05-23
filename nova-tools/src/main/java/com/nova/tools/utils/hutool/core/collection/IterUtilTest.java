package com.nova.tools.utils.hutool.core.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.FilterIter;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * {@link IterUtil} 单元测试
 *
 * @author: looly
 */
public class IterUtilTest {

    @Test
    public void getFirstTest() {
        Assert.isNull(IterUtil.getFirst((Iterable<Object>) null));
        Assert.isNull(IterUtil.getFirst(CollUtil.newArrayList()));

        Assert.equals("1", IterUtil.getFirst(CollUtil.newArrayList("1", "2", "3")));
        final ArrayDeque<String> deque = new ArrayDeque<>();
        deque.add("3");
        deque.add("4");
        Assert.equals("3", IterUtil.getFirst(deque));
    }

    @Test
    public void getFirstNonNullTest() {
        final ArrayList<String> strings = CollUtil.newArrayList(null, null, "123", "456", null);
        Assert.equals("123", IterUtil.getFirstNoneNull(strings));
    }

    @Test
    public void fieldValueMapTest() {
        final ArrayList<Car> carList = CollUtil.newArrayList(new Car("123", "大众"), new Car("345", "奔驰"), new Car("567", "路虎"));
        final Map<String, Car> carNameMap = IterUtil.fieldValueMap(carList.iterator(), "carNumber");

        Assert.equals("大众", carNameMap.get("123").getCarName());
        Assert.equals("奔驰", carNameMap.get("345").getCarName());
        Assert.equals("路虎", carNameMap.get("567").getCarName());
    }

    @Test
    public void joinTest() {
        final ArrayList<String> list = CollUtil.newArrayList("1", "2", "3", "4");
        final String join = IterUtil.join(list.iterator(), ":");
        Assert.equals("1:2:3:4", join);

        final ArrayList<Integer> list1 = CollUtil.newArrayList(1, 2, 3, 4);
        final String join1 = IterUtil.join(list1.iterator(), ":");
        Assert.equals("1:2:3:4", join1);

        // 包装每个节点
        final ArrayList<String> list2 = CollUtil.newArrayList("1", "2", "3", "4");
        final String join2 = IterUtil.join(list2.iterator(), ":", "\"", "\"");
        Assert.equals("\"1\":\"2\":\"3\":\"4\"", join2);
    }

    @Test
    public void joinWithFuncTest() {
        final ArrayList<String> list = CollUtil.newArrayList("1", "2", "3", "4");
        final String join = IterUtil.join(list.iterator(), ":", String::valueOf);
        Assert.equals("1:2:3:4", join);
    }

    @Test
    public void joinWithNullTest() {
        final ArrayList<String> list = CollUtil.newArrayList("1", null, "3", "4");
        final String join = IterUtil.join(list.iterator(), ":", String::valueOf);
        Assert.equals("1:null:3:4", join);
    }

    @Test
    public void testToListMap() {
        final Map<String, List<String>> expectedMap = new HashMap<>();
        expectedMap.put("a", Collections.singletonList("and"));
        expectedMap.put("b", Arrays.asList("brave", "back"));

        final Map<String, List<String>> testMap = IterUtil.toListMap(Arrays.asList("and", "brave", "back"),
                v -> v.substring(0, 1));
        Assert.equals(testMap, expectedMap);
    }

    @Test
    public void testToMap() {
        final Map<String, Car> expectedMap = new HashMap<>();

        final Car bmw = new Car("123", "bmw");
        expectedMap.put("123", bmw);

        final Car benz = new Car("456", "benz");
        expectedMap.put("456", benz);

        final Map<String, Car> testMap = IterUtil.toMap(Arrays.asList(bmw, benz), Car::getCarNumber);
        Assert.equals(expectedMap, testMap);
    }

    @Test
    public void getElementTypeTest() {
        final List<Integer> integers = Arrays.asList(null, 1);
        final Class<?> elementType = IterUtil.getElementType(integers);
        Assert.equals(Integer.class, elementType);
    }

    @Data
    @AllArgsConstructor
    public static class Car {
        private String carNumber;
        private String carName;
    }

    @Test
    public void filterTest() {
        final List<String> obj2 = ListUtil.toList("3");
        final List<String> obj = ListUtil.toList("1", "3");

        IterUtil.filter(obj.iterator(), obj2::contains);

        Assert.equals(1, obj.size());
        Assert.equals("3", obj.get(0));
    }

    @Test
    public void filteredTest() {
        final List<String> obj2 = ListUtil.toList("3");
        final List<String> obj = ListUtil.toList("1", "3");

        final FilterIter<String> filtered = IterUtil.filtered(obj.iterator(), obj2::contains);

        Assert.equals("3", filtered.next());
        Assert.isFalse(filtered.hasNext());
    }

    @Test
    public void filterToListTest() {
        final List<String> obj2 = ListUtil.toList("3");
        final List<String> obj = ListUtil.toList("1", "3");

        final List<String> filtered = IterUtil.filterToList(obj.iterator(), obj2::contains);

        Assert.equals(1, filtered.size());
        Assert.equals("3", filtered.get(0));
    }

    @Test
    public void getTest() {
        final HashSet<String> set = CollUtil.set(true, "A", "B", "C", "D");
        final String str = IterUtil.get(set.iterator(), 2);
        Assert.equals("C", str);
    }
}
