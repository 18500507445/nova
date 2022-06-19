package com.nova.tools.vc.global.dingxin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.nova.tools.utils.list.SplitListUtils.averageAssign;


/**
 * @author wangzehui
 * @date 2018/9/25 10:16
 */

public class SplitList implements Serializable {

    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            integers.add(i);
        }
        List<List<Integer>> lists = averageAssign(integers, integers.size() / 9 + 1);
        System.out.println(integers.size() / 999 + 1);
        System.out.println(lists);


    }
}
