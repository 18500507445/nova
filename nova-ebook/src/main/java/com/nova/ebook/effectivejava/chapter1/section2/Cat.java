package com.nova.ebook.effectivejava.chapter1.section2;

import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 14:59
 */
@Data
@Builder
class Cat {

    private Integer id;

    private String name;
}
