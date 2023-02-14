package com.nova.ebook.effectivejava.chapter2.section2;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:39
 */
class Cat {

    static class BlackCat {
        private Integer id;

        private String name;

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    @EqualsAndHashCode
    static class BlueCat {
        private Integer id;

        private String name;

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
}
