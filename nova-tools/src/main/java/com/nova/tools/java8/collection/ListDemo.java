package com.nova.tools.java8.collection;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.nova.tools.demo.entity.Myself;
import com.nova.tools.demo.entity.People;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @description: list练习
 * @author: wzh
 * @date: 2019/6/14 14:07
 */
class ListDemo {

    public static final List<People> PEOPLE_LIST = Myself.EXERCISE_LIST;

    public static final List<String> STRINGS = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");

    public static final List<Integer> NUMBERS = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

    /**
     * 测试统计list
     */
    @Test
    public void processList() {
        long count = STRINGS.stream().filter(String::isEmpty).count();
        System.err.println("空字符串数量为: " + count);

        count = STRINGS.stream().filter(string -> string.length() == 3).count();
        System.err.println("字符串长度为 3 的数量为: " + count);

        List<String> filtered = STRINGS.stream().filter(string -> !string.isEmpty()).collect(toList());
        System.err.println("筛选后的列表: " + filtered);

        String mergedString = STRINGS.stream().filter(string -> !string.isEmpty()).collect(joining(", "));
        System.err.println("合并字符串: " + mergedString);

        List<Integer> squaresList = NUMBERS.stream().map(i -> i * i).distinct().collect(toList());
        System.err.println("Squares List: " + squaresList);

        SecureRandom random = new SecureRandom();
        random.ints().limit(10).sorted().forEach(System.err::println);

        // 并行处理
        count = STRINGS.parallelStream().filter(String::isEmpty).count();
        System.err.println("空字符串的数量为: " + count);
    }

    /**
     * list循环遍历
     */
    @Test
    public void listForEach() {
        List<People> peopleListNew = new ArrayList<>();
        PEOPLE_LIST.forEach(people -> {
            People peopleNew = new People();
            peopleNew.setId(people.getId());
            peopleNew.setAge(people.getAge());
            peopleNew.setName(people.getName());
            peopleNew.setGroupId(people.getGroupId());
            peopleNew.setGroupName(people.getGroupName());
            peopleNew.setDescription(people.getDescription());
            peopleListNew.add(peopleNew);
        });
        peopleListNew.forEach(System.err::println);
        System.err.println(JSONUtil.toJsonStr(peopleListNew));
    }


    /**
     * 统计list 按照某个属性
     */
    @Test
    public void statisticsList() {
        IntSummaryStatistics stats = PEOPLE_LIST.stream().mapToInt(People::getAge).summaryStatistics();
        System.err.println("max : " + stats.getMax());
        System.err.println("min : " + stats.getMin());
        System.err.println("sum : " + stats.getSum());
        System.err.println("avg : " + stats.getAverage());
    }

    /**
     * list排序 按照某个属性
     */
    @Test
    public void listSorted() {
        //降序
        List<People> descList = PEOPLE_LIST.stream().sorted(Comparator.comparing(People::getAge).reversed()).collect(toList());
        //升序
        List<People> ascList = PEOPLE_LIST.stream().sorted(Comparator.comparing(People::getAge)).collect(toList());

        System.err.println(JSONUtil.toJsonStr(descList));
        System.err.println(JSONUtil.toJsonStr(ascList));

        //todo 排序方式 二 (正序)
        PEOPLE_LIST.sort(Comparator.comparing(People::getAge));
        //倒序
        PEOPLE_LIST.sort((o1, o2) -> o2.getAge().compareTo(o1.getAge()));
        //倒叙
        PEOPLE_LIST.sort(Comparator.comparing(People::getAge).reversed());
    }


    /**
     * list分组 按照某个属性
     */
    @Test
    public void listGrouping() {
        Map<Integer, List<People>> peopleMap = PEOPLE_LIST.stream().collect(groupingBy(People::getGroupId));

        //partitioningBy，二分操作，只会返回2组数据，满足和不满足条件
        Map<Boolean, List<People>> collect = PEOPLE_LIST.stream().collect(partitioningBy(people -> ObjectUtil.equals(1, people.getGroupId())));

        System.out.println("peopleMap = " + JSONUtil.toJsonStr(peopleMap));
        System.out.println("collect = " + JSONUtil.toJsonStr(collect));
    }


    /**
     * list对象 joining
     */
    @Test
    public void listJoin() {
        List<String> nameList = new ArrayList<>();
        for (People people : PEOPLE_LIST) {
            nameList.add(people.getName());
        }
        //方式1
        String nameStr1 = String.join(",", nameList);
        System.err.println(JSONUtil.toJsonStr(nameStr1));

        //方式2
        String nameStr2 = StrUtil.join(",", nameList);
        System.err.println(JSONUtil.toJsonStr(nameStr2));

        //方式3
        String collect = PEOPLE_LIST.stream().map(People::getName).collect(joining(","));
        System.err.println(JSONUtil.toJsonStr(collect));
    }


    /**
     * list过滤 按照指定条件过滤
     */
    @Test
    public void listFilter() {
        //过滤掉二组
        List<People> filterList = PEOPLE_LIST.stream().filter(people -> "一组".equals(people.getGroupName())).collect(toList());
        System.err.println(JSONUtil.toJsonStr(filterList));
    }

    /**
     * list转map，⚠️重复数据v1、v2取v2
     */
    @Test
    public void listToMap() {
        Map<String, Integer> collect = PEOPLE_LIST.stream().collect(toMap(People::getName, People::getAge, (v1, v2) -> v1));
        Map<String, Integer> newCollect = PEOPLE_LIST.stream().collect(toMap(People::getName, People::getAge, (v1, v2) -> v1));
        Map<String, People> peopleMap = PEOPLE_LIST.stream().collect(toMap(People::getName, Function.identity(), (v1, v2) -> v1));

        TreeMap<Integer, Integer> treeMap = PEOPLE_LIST.stream()
                .collect(Collectors.toMap(People::getId, People::getAge,
                        (existingValue, newValue) -> existingValue, TreeMap::new));
        System.err.println(JSONUtil.toJsonStr(collect));
    }

    /**
     * list转object
     */
    @Test
    public void listToObject() {
        List<Object> collect = PEOPLE_LIST.stream().map(People::getAge).collect(toList());

        HashSet<String> hashSet = PEOPLE_LIST.stream().map(People::getName).collect(Collectors.toCollection(HashSet::new));

        System.err.println(JSONUtil.toJsonStr(collect));
    }

    /**
     * list limit
     */
    @Test
    public void listLimit() {
        List<People> collect = PEOPLE_LIST.stream().limit(2).collect(toList());
        System.err.println(JSONUtil.toJsonStr(collect));
    }

    /**
     * list reduce计算
     */
    @Test
    public void listReduce() {
        BigDecimal total = PEOPLE_LIST.stream().map(People::getFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.err.println("bigDecimal-add:" + total);
    }

    /**
     * 求和，最大，最小，平均
     */
    @Test
    public void calculation() {
        //方式一 属性bigDecimal
        BigDecimal maxBigDecimal = PEOPLE_LIST.stream().map(People::getFee).max(BigDecimal::compareTo).get();
        System.err.println("statistics--maxBigDecimal:" + maxBigDecimal);


        //属性double
        double doubleFee = PEOPLE_LIST.stream().mapToDouble(People::getDoubleFee).sum();
        System.err.println("statistics--doubleFee:" + doubleFee);


        //方式二
        IntSummaryStatistics statistics = PEOPLE_LIST.stream().collect(summarizingInt(People::getAge));
        System.err.println("statistics--sum:" + statistics.getSum());
        System.err.println("statistics--max:" + statistics.getMax());
        System.err.println("statistics--min:" + statistics.getMin());
        System.err.println("statistics--avg:" + statistics.getAverage());

        Double doubleAvg = PEOPLE_LIST.stream().collect(averagingInt(People::getAge));
        System.err.println("doubleAvg:" + doubleAvg);

        //方式三
        int sum = PEOPLE_LIST.stream().mapToInt(People::getAge).sum();
        int max = PEOPLE_LIST.stream().mapToInt(People::getAge).max().getAsInt();
        int min = PEOPLE_LIST.stream().mapToInt(People::getAge).min().getAsInt();
        double asDouble = PEOPLE_LIST.stream().mapToInt(People::getAge).average().getAsDouble();
        System.err.println("sum:" + sum);
        System.err.println("max:" + max);
        System.err.println("min:" + min);
        System.err.println("asDouble:" + asDouble);

        //方式四 找出最大、最小的对象
        People maxData = PEOPLE_LIST.stream().max(Comparator.comparing(People::getAge)).get();
        People minData = PEOPLE_LIST.stream().min(Comparator.comparing(People::getAge)).get();
        System.err.println("maxData:" + JSONUtil.toJsonStr(maxData));
        System.err.println("minData:" + JSONUtil.toJsonStr(minData));

    }

    /**
     * list 去重
     */
    @Test
    public void listDistinct() {
        List<String> distinct = PEOPLE_LIST.stream().map(People::getName).distinct().collect(toList());
        System.err.println("distinct:" + JSONUtil.toJsonStr(distinct));
    }


    /**
     * list match匹配
     */
    @Test
    public void listMatch() {
        //所有人年龄是否都大于14岁
        boolean allResult = PEOPLE_LIST.stream().allMatch(i -> i.getAge().compareTo(14) > 0);
        System.err.println("allResult:" + allResult);

        //是否有大于14岁
        boolean oneResult = PEOPLE_LIST.stream().anyMatch(i -> i.getAge().compareTo(14) > 0);
        System.err.println("oneResult:" + oneResult);
    }


    /**
     * list find查找元素
     * list取第一个
     */
    @Test
    public void listFind() {
        //orElse或者直接get()
        People first = PEOPLE_LIST.stream().findFirst().orElse(null);
        System.err.println("findFirst:" + JSONUtil.toJsonStr(first));

        //并行的情况，那就不能确保是第一个，串行时数据较少findAny()是为了更高效
        People any = PEOPLE_LIST.stream().findAny().get();
        System.err.println("findAny:" + JSONUtil.toJsonStr(any));
    }

    /**
     * 并行流
     */
    @Test
    public void parallelStream() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        // 串行流
        int sum1 = list.stream().reduce(5, Integer::sum);
        // 并行流
        int sum2 = list.parallelStream().reduce(5, Integer::sum);
        System.err.println("串行流执行结果：" + sum1);
        System.err.println("并行流执行结果：" + sum2);

        int sum3 = list.parallelStream().reduce(0, Integer::sum) + 5;
        System.err.println("并行流之外加5执行结果：" + sum3);
    }

}
