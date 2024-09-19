package com.nova.book.effectivejava.chapter6.section6;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest6<T> {

    /**
     * 模拟查询或调用接口拿到数据，做处理判断，尽量返回空list
     *
     * @param param
     * @return
     */
    public List<T> getList(Object param) {
        List<T> list = new ArrayList<>();
        if (param == null) {
            return Collections.emptyList();
        }
        return list;
    }
}
