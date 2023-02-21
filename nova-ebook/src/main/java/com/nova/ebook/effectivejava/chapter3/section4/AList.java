package com.nova.ebook.effectivejava.chapter3.section4;

import java.util.Collection;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/16 09:28
 */
class AList<T> {

    public void add(T t) {
        System.out.println("add");
    }

    public void addAll(Collection<T> coll) {
        if (null == coll || coll.isEmpty()) {
            return;
        }
        for (T t : coll) {
            add(t);
        }
        System.out.println("addAll");
    }
}

class BList<T> extends AList<T> {

    private int count = 0;

    @Override
    public void add(T t) {
        count++;
        super.add(t);
    }

    @Override
    public void addAll(Collection<T> coll) {
        if (null == coll || coll.isEmpty()) {
            return;
        }

        /***
         * 父类调用add(t),b类实现了count++
         * 需要考虑父类的实现方式才能确定此方法的写法
         */
        //count += coll.size();

        super.addAll(coll);
    }

    public int addCount() {
        return count;
    }
}

class CList<T> {

    private AList aList = new AList();

    private int count = 0;

    public void add(T t) {
        count++;
        aList.add(t);
    }

    public void addAll(Collection<T> coll) {
        if (null == coll || coll.isEmpty()) {
            return;
        }
        for (T t : coll) {
            count++;
            aList.add(t);
        }
    }

    public int addCount() {
        return count;
    }
}
