package com.nova.ebook.effectivejava.chapter6.section2;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/26 14:06
 */
@Data
final class Cat {

    private final Date date;

    public Date getDate() {
        return date;
    }

    public Cat(Date time) {
        this.date = time;
    }
}
