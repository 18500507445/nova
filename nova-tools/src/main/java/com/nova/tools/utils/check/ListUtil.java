package com.nova.tools.utils.check;

import java.util.List;

/**
 * created in 2019/5/16 16:03.
 */
public class ListUtil {
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
}
