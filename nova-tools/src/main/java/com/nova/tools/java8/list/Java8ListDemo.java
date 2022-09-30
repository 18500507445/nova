package com.nova.tools.java8.list;

import cn.hutool.json.JSONUtil;
import com.nova.tools.demo.entity.Myself;
import com.nova.tools.demo.entity.People;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @Description: list练习
 * @Author: wangzehui
 * @Date: 2019/6/14 14:07
 */

public class Java8ListDemo {

    public static void main(String[] args) {
        List<People> peopleList = Myself.EXERCISE_LIST;
        calculation(peopleList);
    }

    /**
     * list循环遍历
     */
    private static void listForEach(List<People> peopleList) {
        List<People> peopleListNew = new ArrayList<>();
        peopleList.forEach(people -> {
            People peopleNew = new People();
            peopleNew.setId(people.getId());
            peopleNew.setAge(people.getAge());
            peopleNew.setName(people.getName());
            peopleNew.setGroupId(people.getGroupId());
            peopleNew.setGroupName(people.getGroupName());
            peopleNew.setDescription(people.getDescription());
            peopleListNew.add(peopleNew);
        });
        peopleListNew.forEach(System.out::println);
        System.out.println(JSONUtil.toJsonStr(peopleListNew));

    }


    /**
     * 统计list 按照某个属性
     */
    private static void statisticsList(List<People> peopleList) {
        IntSummaryStatistics stats = peopleList.stream().mapToInt(People::getAge).summaryStatistics();
        System.out.println("max : " + stats.getMax());
        System.out.println("min : " + stats.getMin());
        System.out.println("sum : " + stats.getSum());
        System.out.println("avg : " + stats.getAverage());
    }

    /**
     * list排序 按照某个属性
     */
    private static void listSorted(List<People> peopleList) {
        //降序
        List<People> descList = peopleList.stream().sorted(Comparator.comparing(People::getAge).reversed()).collect(Collectors.toList());
        //升序
        List<People> ascList = peopleList.stream().sorted(Comparator.comparing(People::getAge)).collect(Collectors.toList());

        System.out.println(JSONUtil.toJsonStr(descList));
        System.out.println(JSONUtil.toJsonStr(ascList));

        //todo 排序方式 二 (正序)
        peopleList.sort(Comparator.comparing(People::getAge));
        //倒序
        peopleList.sort((o1, o2) -> o2.getAge().compareTo(o1.getAge()));
        //倒叙
        peopleList.sort(Comparator.comparing(People::getAge).reversed());
    }


    /**
     * list分组 按照某个属性
     */
    private static void listGrouping(List<People> peopleList) {
        Map<Integer, List<People>> peopleMap = peopleList.stream().collect(Collectors.groupingBy(People::getGroupId));
        System.out.println(JSONUtil.toJsonStr(peopleMap));
    }


    /**
     * list对象 joining
     */
    private static void listJoin(List<People> peopleList) {
        List<String> nameList = new ArrayList<>();
        for (People people : peopleList) {
            nameList.add(people.getName());
        }
        //方式1
        String nameStr1 = String.join(",", nameList);

        //方式2
        String nameStr2 = StringUtils.join(nameList, ",");

        //方式3
        String collect = peopleList.stream().map(People::getName).collect(joining(","));


        System.out.println(JSONUtil.toJsonStr(nameStr1));
        System.out.println(JSONUtil.toJsonStr(nameStr2));
        System.out.println(JSONUtil.toJsonStr(collect));
    }


    /**
     * list过滤 按照指定条件过滤
     */
    private static void listFilter(List<People> peopleList) {
        //过滤掉二组
        List<People> filterList = peopleList.stream().filter(people -> "一组".equals(people.getGroupName())).collect(Collectors.toList());
        System.out.println(JSONUtil.toJsonStr(filterList));
    }

    /**
     * list转map
     */
    private static void listToMap(List<People> people) {
        Map<String, Integer> collect = people.stream().collect(toMap(People::getName, People::getAge));
        System.out.println(JSONUtil.toJsonStr(collect));

    }

    /**
     * list转object
     *
     * @param people
     */
    private static void listToObject(List<People> people) {
        List<Object> collect = people.stream().map(People::getAge).collect(toList());
        System.out.println(JSONUtil.toJsonStr(collect));
    }

    /**
     * list limit
     */
    private static void listLimit(List<People> people) {
        List<People> collect = people.stream().limit(2).collect(toList());
        System.out.println(JSONUtil.toJsonStr(collect));
    }

    /**
     * list reduce
     */
    private static void listReduce(List<People> people) {
        BigDecimal total = people.stream().map(People::getFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("bigDecimal-add:" + total);
    }

    /**
     * 求和，最大，最小，平均
     */
    private static void calculation(List<People> people) {
        //方式一 属性bigDecimal
        BigDecimal maxBigDecimal = people.stream().map(People::getFee).max(BigDecimal::compareTo).get();

        //方式二
        IntSummaryStatistics statistics = people.stream().collect(summarizingInt(People::getAge));
        System.out.println("statistics--sum:" + statistics.getSum());
        System.out.println("statistics--max:" + statistics.getMax());
        System.out.println("statistics--min:" + statistics.getMin());
        System.out.println("statistics--avg:" + statistics.getAverage());

        Double doubleAvg = people.stream().collect(averagingInt(People::getAge));
        System.out.println("doubleAvg:" + doubleAvg);

        //方式三
        int sum = people.stream().mapToInt(People::getAge).sum();
        int max = people.stream().mapToInt(People::getAge).max().getAsInt();
        int min = people.stream().mapToInt(People::getAge).min().getAsInt();
        double asDouble = people.stream().mapToInt(People::getAge).average().getAsDouble();
        System.out.println("sum:" + sum);
        System.out.println("max:" + max);
        System.out.println("min:" + min);
        System.out.println("asDouble:" + asDouble);

        //方式四 找出最大、最小的对象
        People maxData = people.stream().max(Comparator.comparing(People::getAge)).get();
        People minData = people.stream().min(Comparator.comparing(People::getAge)).get();
        System.out.println("maxData:" + JSONUtil.toJsonStr(maxData));
        System.out.println("minData:" + JSONUtil.toJsonStr(minData));
    }

    /**
     * list 去重
     */
    private static void listDistinct(List<People> people) {
        List<String> distinct = people.stream().map(People::getName).distinct().collect(toList());
        System.out.println("distinct:" + JSONUtil.toJsonStr(distinct));
    }


    /**
     * list match匹配
     */
    private static void listMatch(List<People> people) {
        //所有人年龄是否都大于14岁
        boolean allResult = people.stream().allMatch(i -> i.getAge().compareTo(14) > 0);
        System.out.println("allResult:" + allResult);

        //是否有大于14岁
        boolean oneResult = people.stream().allMatch(i -> i.getAge().compareTo(14) > 0);
        System.out.println("oneResult:" + oneResult);
    }


    /**
     * list find查找元素
     */
    private static void listFind(List<People> people) {
        People first = people.stream().findFirst().get();
        System.out.println("findFirst:" + JSONUtil.toJsonStr(first));

        //并行的情况，那就不能确保是第一个，串行时数据较少findAny()是为了更高效
        People any = people.stream().findAny().get();
        System.out.println("findAny:" + JSONUtil.toJsonStr(any));

    }


}
