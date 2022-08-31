package com.nova.tools.demo.list;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.nova.tools.demo.entity.Myself;
import com.nova.tools.demo.entity.People;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @Description: list练习
 * @Author: wangzehui
 * @Date: 2019/6/14 14:07
 */

public class ListExercise {

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
        System.out.println(JSONObject.toJSONString(peopleListNew));

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
    private static void listSort(List<People> peopleList) {
        //降序
        List<People> descList = peopleList.stream().sorted(Comparator.comparing(People::getAge).reversed()).collect(Collectors.toList());
        //升序
        List<People> ascList = peopleList.stream().sorted(Comparator.comparing(People::getAge)).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(descList));
    }


    /**
     * list排序 按照某个属性
     */
    private static void listSortNew(List<People> peopleList) {
        peopleList.sort(Comparator.comparing(People::getAge).reversed());
        System.out.println(JSONObject.toJSONString(peopleList));
    }

    /**
     * list分组 按照某个属性
     */
    private static void listGrouping(List<People> peopleList) {
        Map<Integer, List<People>> peopleMap = peopleList.stream().collect(Collectors.groupingBy(People::getGroupId));
        System.out.println(JSONObject.toJSONString(peopleMap));
    }


    /**
     * list对象 逗号拼接成字符串
     */
    private static void listToStrJoinSymbol(List<People> peopleList) {
        List<String> nameList = new ArrayList<>();
        for (People people : peopleList) {
            nameList.add(people.getName());
        }
        //方式1
        String nameStr1 = String.join(",", nameList);

        //方式2
        String nameStr2 = StringUtils.join(nameList, ",");

        System.out.println(JSONObject.toJSONString(nameStr1));
    }

    /**
     * list过滤 按照指定条件过滤
     */
    private static void listFilter(List<People> peopleList) {
        //过滤掉二组
        List<People> filterList = peopleList.stream().filter(people -> "一组".equals(people.getGroupName())).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(filterList));
    }

    /**
     * list转map
     */
    private static void ToMap(List<People> people) {
        Map<String, Integer> collect = people.stream().collect(toMap(People::getName, People::getAge));
        System.out.println(JSONUtil.toJsonStr(collect));
    }

    /**
     * 计算 求和，最大，最小，平均
     */
    private static void calculation(List<People> people) {
        IntSummaryStatistics collect = people.stream().collect(summarizingInt(People::getAge));
        System.out.println(collect.getSum());
        System.out.println(collect.getMax());
        System.out.println(collect.getMin());
        System.out.println(collect.getAverage());


        Double doubleAvg = people.stream().collect(averagingInt(People::getAge));
        System.out.println(doubleAvg);

        int sum = people.stream().mapToInt(People::getAge).sum();
        int max = people.stream().mapToInt(People::getAge).max().getAsInt();
        int min = people.stream().mapToInt(People::getAge).min().getAsInt();
        double asDouble = people.stream().mapToInt(People::getAge).average().getAsDouble();
        System.out.println(sum);
    }

}
