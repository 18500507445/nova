package com.nova.tools.utils.list;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;

/**
 * @Description wangzehui
 * <p>
 * https://www.cnblogs.com/stupidMartian/p/9894454.html（原地址）
 * https://blog.csdn.net/biewan0238/article/details/103061933 (true 和false排序)
 * @Date 2020/7/1 13:48
 */
public class ListUtils {
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
                e.printStackTrace();
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
                e.printStackTrace();
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
            int maxlen = Math.max(str1.length(), str2.length());
            str1 = ListUtils.addZero2Str((Number) value1, maxlen);
            str2 = ListUtils.addZero2Str((Number) value2, maxlen);
        } else if (value1 instanceof Date && value2 instanceof Date) {
            long time1 = ((Date) value1).getTime();
            long time2 = ((Date) value2).getTime();
            int maxlen = Long.toString(Math.max(time1, time2)).length();
            str1 = ListUtils.addZero2Str(time1, maxlen);
            str2 = ListUtils.addZero2Str(time2, maxlen);
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
        Object object = null;
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
     * 获取最大连续重复次数
     *
     * @param list
     * @return
     */
    private int getMaxHitNum(List<String> list) {
        //未命中次数、命中次数、最大连续未命中次数次数、最大连续命中次数
        int unHit = 0, hit = 0, maxUnHit = 0, maxHit = 0;
        for (String s : list) {
            //2未命中，1命中
            if ("2".equals(s)) {
                unHit++;   //如果有未命中的，需要把累计的命中次数清零
                hit = 0;
            } else if ("1".equals(s)) {
                hit++;
                unHit = 0;
            }
            if (unHit > maxUnHit) {
                maxUnHit = unHit;
            }
            if (hit > maxHit) {
                maxHit = hit;
            }
        }
        return maxUnHit;
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
        List setTmp = new LinkedList();
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
     * 首先进行入参检查防止出现空指针异常
     * 如果两个参数都为空，则返回true
     * 如果有一项为空，则返回false
     * 接着对第一个list进行遍历，如果某一项第二个list里面没有，则返回false
     * 还要再将两个list反过来比较，因为可能一个list是两一个list的子集
     * 如果成功遍历结束，返回true
     *
     * @param l0
     * @param l1
     * @return
     */
    public static boolean isListEqual(List l0, List l1) {
        if (l0 == l1) {
            return true;
        }
        if (l0 == null && l1 == null) {
            return true;
        }
        if (l0 == null || l1 == null) {
            return false;
        }
        if (l0.size() != l1.size()) {
            return false;
        }
        for (Object o : l0) {
            if (!l1.contains(o)) {
                return false;
            }
        }
        for (Object o : l1) {
            if (!l0.contains(o)) {
                return false;
            }
        }
        return true;
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

    public static void main(String[] args) {
        List<Integer> o1 = Arrays.asList(1, 2, 3);
        List<Integer> o2 = Arrays.asList(4, 5, 6);
        List<Integer> integers = overlappingList(o1, o2);
        System.out.println(integers);
    }
}
