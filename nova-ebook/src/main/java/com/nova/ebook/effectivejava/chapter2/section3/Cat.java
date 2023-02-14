package com.nova.ebook.effectivejava.chapter2.section3;

import lombok.Data;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/14 19:22
 */
class Cat {

    static class BlackCat{
        private Integer id;

        private String name;

        @Override
        public String toString() {
            return "BlackCat{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Data
    static class BlueCat{
        private Integer id;

        private String name;

    }
}
