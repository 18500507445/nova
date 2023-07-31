package com.nova.book.effectivejava.chapter6.section5;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest5 {

    public void testArgs(String... args) {
        System.err.println("args.getClass() = " + args.getClass());
        for (String arg : args) {
            System.err.println(arg);
        }
    }

    /**
     * 可以设计成这样的
     * @param args0
     * @param otherArgs
     */
    public void args(Object args0, Object... otherArgs) {

    }

    @Test
    public void demoA() {
        testArgs();
        testArgs("a");
        testArgs("a", "b");
        testArgs("a", null, "b");
    }


}
