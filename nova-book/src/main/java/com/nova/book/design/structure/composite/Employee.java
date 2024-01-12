package com.nova.book.design.structure.composite;

import lombok.Data;
import lombok.Getter;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @description: 员工
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
public class Employee {

    private String name;
    private String dept;

    @Getter
    private List<Employee> subordinates;

    public Employee(String name, String dept) {
        this.name = name;
        this.dept = dept;
        this.subordinates = Lists.newArrayList();
    }

    public void add(Employee e) {
        this.subordinates.add(e);
    }

    public void remove(Employee e) {
        this.subordinates.remove(e);
    }

}