package com.nova.tools.utils.guava;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 18:26
 */
class GuavaTest {

    @Test
    public void test8() {
        String param = null;
//        if (param == null) {
//            throw new RuntimeException("参数不能为空");
//        }
//        Preconditions.checkNotNull(param,"参数不能为空");
        // 第一个expression:布尔值的表达式，true:校验通过，不抛异常;false:抛异常
        Preconditions.checkArgument(param != null, "参数不能为空");
    }

    /**
     * 不可变集合
     */
    @Test
    public void test7() {
        List<String> list = new ArrayList<>();
        list.add("aa");
        // 把list放到了缓存里面
        // 怎么强制约束别人不能改我的list？
        ImmutableList<Object> immutableList = ImmutableList.builder().add("aa").build();
        // 把immutableList放到缓存中,如果执行add操作会报错:UnsupportedOperationException
//        immutableList.add("bbb");

        List<String> jdkUnmodifiableList = Collections.unmodifiableList(list);
        // 如果执行add操作会报错:UnsupportedOperationException
//        jdkUnmodifiableList.add("ccc");
        list.add("ccc");
        System.err.println(jdkUnmodifiableList);
    }

    /**
     * HashMultimap用来替代jdk原生的Map<String,Collection<String>> map;
     */
    @Test
    public void test6() {
        Multimap<String, String> multimap = HashMultimap.create();
        multimap.put("a", "1");
        multimap.put("a", "2");
        multimap.put("a", "3");
        Collection<String> aValues = multimap.get("a");
        System.err.println(aValues);

        // 是否包含key=a,value=1的entry
        System.err.println(multimap.containsEntry("a", "4"));
        // 转化成jdk原生api实现的数据结构
        Map<String, Collection<String>> jdkMap = multimap.asMap();
        System.err.println(jdkMap);
    }

    /**
     * Multiset
     * list:元素可重复的有序集合
     * set:元素不可重复的无序集合
     */
    @Test
    public void test5() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("a");
        multiset.add("b");
        multiset.add("c");
        multiset.add("a");
        System.err.println(multiset);
        Set<Multiset.Entry<String>> entries = multiset.entrySet();
        System.err.println(entries);// [a x 2, b, c]
        for (Multiset.Entry<String> entry : entries) {
            System.err.println("元素:" + entry.getElement() + ",个数:" + entry.getCount());
        }

        Set<String> elementSet = multiset.elementSet();
        System.err.println(elementSet);// [a, b, c]
        for (String ele : elementSet) {
            System.err.println("集合里面的元素:" + ele);
        }
    }

    /**
     * Ints
     * Longs
     * Floats
     * ...
     */
    @Test
    public void test4() {
        List<Integer> integers = Ints.asList(1, 2, 3);
        System.err.println(integers);
    }

    /**
     * Lists的用法,另有Sets、Maps
     */
    @Test
    public void test3() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        // 提供集合的快速创建方式
        ArrayList<String> list2 = Lists.newArrayList("a", "b", "c");
        /*
        要求你传ids,一次最多传2个
         */
        // 把list2分成小的集合，小的集合大小是size
//        list2.subList() // 太麻烦
        List<List<String>> partition = Lists.partition(list2, 2);
        System.err.println(partition);
        for (List<String> ids : partition) {
//            api(ids);
        }
    }

    /**
     * 下划线和驼峰互转
     * student_name
     * studentName
     */
    @Test
    public void test2() {
        String str = "student_name";
        // 下划线转驼峰 CAMEL:骆驼
        // studentName
        System.err.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str));
        // StudentName
        System.err.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str));

        // 驼峰转下划线
        str = "studentName";
        // 结果: student_name
        System.err.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str));
    }

    /**
     * Joiner:把集合(或数组或可变参数)通过指定的分隔符连接成字符串
     * Splitter：通过指定的分隔符把字符串转为集合
     */
    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add(null);

        Joiner joiner = Joiner.on(",")
                // 忽略null
//                .skipNulls()
                .useForNull("这是null的替代物");

        // a,b,c
        System.err.println(joiner.join(list));
        // jdk8中实现这类需求也比较方便
        System.err.println(list.stream().filter(StrUtil::isNotBlank)
                .collect(Collectors.joining(",")));

        String str = "a,b,\"\",,  c  ,";
        // on：指定字符串的分隔符
        Splitter splitter = Splitter.on(",")
                // 过滤掉空白的字符串(不包括"")
                .omitEmptyStrings()
                // 去除每个元素的前后空格
                .trimResults();
        Iterable<String> iterable = splitter.split(str);
        System.err.println(iterable);
        List<String> splitToList = splitter.splitToList(str);
        System.err.println(splitToList);

    }
}
