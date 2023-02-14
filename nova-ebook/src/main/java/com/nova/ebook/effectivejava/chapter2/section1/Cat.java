package com.nova.ebook.effectivejava.chapter2.section1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 19:45
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class Cat {

    private Integer id;

    private String name;

    private String colour;

    @Override
    public boolean equals(Object param) {
        if (param == this) {
            return true;
        }
        if (param instanceof Cat) {
            Cat cat = (Cat) param;
            return Objects.equals(id, cat.id) && Objects.equals(name, cat.name) && Objects.equals(colour, cat.colour);
        }
        return false;
    }
}
