package com.nova.book.effectivejava.chapter5.section5;

import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest5 {

    @Test
    public void demoA() {
        double add = OperationEnum.calculation(OperationEnum.ADD, 22, 3);
        System.err.println("add = " + add);
    }

}
