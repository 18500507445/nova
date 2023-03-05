package com.nova.book.effectivejava.chapter2.section1;

import java.util.Objects;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/13 20:05
 */
class BlackCat extends Cat {

    public BlackCat() {
        super();
        super.setColour("black");
    }

    @Override
    public boolean equals(Object param) {
        if (param == this) {
            return true;
        }
        if (param instanceof BlackCat) {
            BlackCat cat = (BlackCat) param;
            return Objects.equals(getId(), cat.getId()) && Objects.equals(getName(), cat.getName());
        }
        return false;
    }
}
