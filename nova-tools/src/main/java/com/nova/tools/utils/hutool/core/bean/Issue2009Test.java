package com.nova.tools.utils.hutool.core.bean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * https://github.com/dromara/hutool/issues/2009
 */
public class Issue2009Test {

    @SuppressWarnings("InnerClassMayBeStatic")
    public class BaseA {
        private String paPss;

        public String getPaPss() {
            return paPss;
        }

        public void setPaPss(String paPss) {
            this.paPss = paPss;
        }
    }


    public class A extends BaseA {
        private String papss;

        public String getPapss() {
            return papss;
        }

        public void setPapss(String papss) {
            this.papss = papss;
        }

        @Override
        public String toString() {
            return "A{" +
                    "papss='" + papss + '\'' +
                    '}';
        }
    }


    public class B extends BaseA {
        private String papss;

        public B(String papss) {
            this.papss = papss;
        }

        public String getPapss() {
            return papss;
        }

        public void setPapss(String papss) {
            this.papss = papss;
        }

        @Override
        public String toString() {
            return "B{" +
                    "papss='" + papss + '\'' +
                    '}';
        }
    }

    @Test
    public void test() {
        B b = new B("a string text");
        A a = new A();
        BeanUtil.copyProperties(b, a);

        Assert.equals(b.getPapss(), a.getPapss());
    }
}
