package com.nova.book.effectivejava.chapter3.section9;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 13:19
 */
@Data
@AllArgsConstructor
class Cat {

    private Integer id;

    private String name;

    private Integer age;
}

class CatComparator {

    public static Comparator<Cat> idComparator() {
        return new Comparator<Cat>() {
            @Override
            public int compare(Cat o1, Cat o2) {
                return o1.getId() - o2.getId();
            }
        };
    }

    public static Comparator<Cat> ageComparator() {
        return new Comparator<Cat>() {
            @Override
            public int compare(Cat o1, Cat o2) {
                return o1.getAge() - o2.getAge();
            }
        };
    }
}


