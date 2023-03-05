package com.nova.book.effectivejava.chapter5.section3;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest3 {

    public enum Style {

        BOLD(1), ITALIC(2), UNDERLINE(4), STRIKETHROUGH(8);

        private Integer code;

        Style(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

    public static void applystyles(EnumSet<Style> style) {

    }

    @Test
    public void demoA() {
        System.out.println(1 | 2);
        System.out.println(1 | 4);
        System.out.println(2 | 4);
        System.out.println(1 | 2 | 4);
    }

    @Test
    public void demoB() {
        //applystyles(EnumSet.copyOf(new HashSet<>()));

        EnumSet<Style> styles = EnumSet.of(Style.BOLD, Style.UNDERLINE);

        System.out.println(styles.contains(Style.UNDERLINE));
        System.out.println(styles.contains(Style.STRIKETHROUGH));
        System.out.println(styles);
    }

}
