package com.nova.common.utils.list;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wzh
 * <p>
 * <a href="https://www.cnblogs.com/stupidMartian/p/9894454.html">java list按照 对象指定多个字段属性进行排序</a>（原地址）
 * <a href="https://blog.csdn.net/biewan0238/article/details/103061933">...</a> (true 和false排序)
 * @date: 2020/7/1 13:48
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ListUtils {

    private static final Integer MAX_NUMBER = 2;

    /**
     * 对list的元素按照多个属性名称排序,
     * list元素的属性可以是数字（byte、short、int、long、float、double等，支持正数、负数、0）、char、String、java.util.Date
     *
     * @param list
     * @param sortNameArr list元素的属性名称
     * @param isAsc       true升序，false降序
     */
    public static <E> void sort(List<E> list, final boolean isAsc, final String... sortNameArr) {
        list.sort((a, b) -> {
            int ret = 0;
            try {
                for (String s : sortNameArr) {
                    ret = ListUtils.compareObject(s, isAsc, a, b);
                    if (0 != ret) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("异常信息:", e);
            }
            return ret;
        });
    }

    /**
     * 给list的每个属性都指定是升序还是降序
     *
     * @param list
     * @param sortNameArr 参数数组
     * @param typeArr     每个属性对应的升降序数组， true升序，false降序
     */

    public static <E> void sort(List<E> list, final String[] sortNameArr, final boolean[] typeArr) {
        if (sortNameArr.length != typeArr.length) {
            throw new RuntimeException("属性数组元素个数和升降序数组元素个数不相等");
        }
        list.sort((a, b) -> {
            int ret = 0;
            try {
                for (int i = 0; i < sortNameArr.length; i++) {
                    ret = ListUtils.compareObject(sortNameArr[i], typeArr[i], a, b);
                    if (0 != ret) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("异常信息:", e);
            }
            return ret;
        });
    }

    /**
     * 对2个对象按照指定属性名称进行排序
     *
     * @param sortName 属性名称
     * @param isAsc    true升序，false降序
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    private static <E> int compareObject(final String sortName, final boolean isAsc, E a, E b) throws Exception {
        int ret;
        Object value1 = ListUtils.forceGetFieldValue(a, sortName);
        Object value2 = ListUtils.forceGetFieldValue(b, sortName);
        String str1 = value1.toString();
        String str2 = value2.toString();
        if (value1 instanceof Number && value2 instanceof Number) {
            int maxLen = Math.max(str1.length(), str2.length());
            str1 = ListUtils.addZero2Str((Number) value1, maxLen);
            str2 = ListUtils.addZero2Str((Number) value2, maxLen);
        } else if (value1 instanceof Date && value2 instanceof Date) {
            long time1 = ((Date) value1).getTime();
            long time2 = ((Date) value2).getTime();
            int maxLen = Long.toString(Math.max(time1, time2)).length();
            str1 = ListUtils.addZero2Str(time1, maxLen);
            str2 = ListUtils.addZero2Str(time2, maxLen);
        }
        if (isAsc) {
            ret = str1.compareTo(str2);
        } else {
            ret = str2.compareTo(str1);
        }
        return ret;
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     * <p>
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     *
     * @param numObj 数字对象
     * @param length 指定的长度
     * @return
     */
    public static String addZero2Str(Number numObj, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        return nf.format(numObj);
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param obj       属性名称所在的对象
     * @param fieldName 属性名称
     * @return
     * @throws Exception
     */
    public static Object forceGetFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        Object object;
        boolean accessible = field.isAccessible();
        if (!accessible) {
            // 如果是private,protected修饰的属性，需要修改为可以访问的
            field.setAccessible(true);
            object = field.get(obj);
            // 还原private,protected属性的访问性质
            field.setAccessible(accessible);
            return object;
        }
        object = field.get(obj);
        return object;
    }

    /**
     * map分组
     *
     * @param list
     * @param oneMapKey
     * @return
     */
    public static Map<String, Object> change(List<Map<String, Object>> list, String oneMapKey) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        List<Object> setTmp = new LinkedList<>();
        for (Map<String, Object> tmp : list) {
            setTmp.add(tmp.get(oneMapKey));
        }
        for (Object o : setTmp) {
            String oneSetTmpStr = (String) o;
            List<Map<String, Object>> oneSetTmpList = new ArrayList<>();
            for (Map<String, Object> tmp : list) {
                String oneMapValueStr = (String) tmp.get(oneMapKey);
                if (oneMapValueStr.equals(oneSetTmpStr)) {
                    oneSetTmpList.add(tmp);
                }
            }
            resultMap.put(oneSetTmpStr, oneSetTmpList);
        }
        return resultMap;
    }

    /**
     * 交叉输出集合list 使用链表list处理
     *
     * @param o1
     * @param o2
     * @return
     */
    public static <T> List<T> overlappingList(List<T> o1, List<T> o2) {
        //分别将2个list放入链表队列
        LinkedList<T> list1 = new LinkedList<>(o1);
        LinkedList<T> list2 = new LinkedList<>(o2);

        //计算较大的数组长度
        List<Integer> nums = new ArrayList<>();
        nums.add(list1.size());
        nums.add(list2.size());
        int max = Collections.max(nums);

        //新建一个数组list，来接受最终结果
        List<T> list = new ArrayList<>(o1.size() + o2.size());

        //遍历较大长度，保证所有数据都能取到
        for (int i = 0; i < max; i++) {
            //如果队列还没取完，继续取
            if (!list1.isEmpty()) {
                list.add(list1.poll());
            }
            if (!list2.isEmpty()) {
                list.add(list2.poll());
            }
        }
        return list;
    }


    /**
     * 获取两个集合的差集
     *
     * @param big   大集合
     * @param small 小集合
     * @return 两个集合的差集 big left join small
     */
    public static <T> Collection<T> getDiffSection(Collection<T> big, Collection<T> small) {
        Set<T> differenceSet = Sets.difference(Sets.newHashSet(big), Sets.newHashSet(small));
        return Lists.newArrayList(differenceSet);
    }


    /**
     * 获取两个集合的交集
     * <p>
     * 还有一种方式：CollUtil.disjunction(huTool)
     *
     * @param c1
     * @param c2
     * @return c1 inner join c2
     */
    public static <T> List<T> getInterSection(Collection<T> c1, Collection<T> c2) {
        Set<T> intersections = Sets.intersection(Sets.newHashSet(c1), Sets.newHashSet(c2));
        return Lists.newArrayList(intersections);
    }

    /**
     * 获取两个集合的并集
     *
     * @param c1
     * @param c2
     * @return
     */
    public static <T> List<T> getUnionSection(Collection<T> c1, Collection<T> c2) {
        c1.addAll(c2);
        Set<T> newHashSet = Sets.newHashSet(c1);
        return Lists.newArrayList(newHashSet);
    }

    private static void cutList(List<Integer> list, int limit) {
        //方法一：使用流遍历操作
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            List<Integer> collect = list.stream().skip((long) i * MAX_NUMBER).limit(MAX_NUMBER).collect(Collectors.toList());
            System.err.println(collect);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("异常信息:", e);
            }
        });
    }

    /**
     * 计算切分次数
     */
    private static Integer countStep(Integer size) {
        return (size + MAX_NUMBER - 1) / MAX_NUMBER;
    }


    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     * <p>
     * 新方法guava：Lists.partition(list, n)，n代表单个list max长度
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        if (n <= 0) {
            n = 1;
        }
        List<List<T>> result = new ArrayList<>();
        //(先计算出余数)
        int remaider = source.size() % n;
        //然后是商
        int number = source.size() / n;
        //偏移量
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 开始分页
     *
     * @param list
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     */
    public static <T> List<T> startPage(List<T> list, Integer pageNum, Integer pageSize) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 记录总数
        int count = list.size();
        // 页数
        int pageCount;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        // 开始索引
        int fromIndex;
        // 结束索引
        int toIndex;
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        if (fromIndex < list.size()) {
            return list.subList(fromIndex, toIndex);
        } else {
            return new ArrayList<>();
        }
    }


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        int limit = countStep(list.size());
        cutList(list, limit);

        List<List<Integer>> partition = Lists.partition(list, 5);
        System.err.println(JSONUtil.toJsonStr(partition));
    }
}
