package com.nova.book.effectivejava.chapter1.section2;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 14:59
 */
@Data
@SuperBuilder
class Cat {

    private Integer id;

    private String name;
}
