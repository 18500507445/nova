package com.nova.book.effectivejava.chapter9.section6;

/**
 * @description:
 * @author: wzh
 * @date: 2023/3/1 10:01
 */
class C {

    private A a;

    public void initA() {
        if (null == a) {
            synchronized (this) {
                if (null == a) {
                    a = new A();
                }
            }
        }
    }


}
