package com.nova.tools.java8.list;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/6/14 15:26
 */
public class Java8StringDemo {
    
    public static final List<String> STRINGS = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
    
    public static final List<Integer> NUMBERS = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

    public static void main(String[] args) {

        long count = STRINGS.stream().filter(String::isEmpty).count();
        System.out.println("空字符串数量为: " + count);

        count = STRINGS.stream().filter(string -> string.length() == 3).count();
        System.out.println("字符串长度为 3 的数量为: " + count);

        List<String> filtered = STRINGS.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println("筛选后的列表: " + filtered);

        String mergedString = STRINGS.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

        List<Integer> squaresList = NUMBERS.stream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.println("Squares List: " + squaresList);

        SecureRandom random = new SecureRandom();
        random.ints().limit(10).sorted().forEach(System.out::println);

        // 并行处理
        count = STRINGS.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量为: " + count);
    }
}
