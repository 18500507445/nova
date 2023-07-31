package com.nova.book.design.structure.composite;


import org.junit.jupiter.api.Test;

/**
 * @description: 组合模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        Employee ceo = new Employee("wzh", "ceo");

        Employee hr = new Employee("hr-li", "hr");
        Employee technology = new Employee("it-dog", "technology");

        ceo.add(hr);
        ceo.add(technology);

        // 打印该组织的所有员工
        ceo.getSubordinates().forEach(System.err::println);
    }

}