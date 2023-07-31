package com.nova.tools.demo.exercise;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @description: lombok练习
 * @author: wzh
 * @date: 2023/2/13 18:06
 */
class LombokTest {

    @Data
    static class DemoA {
        private String name;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Slf4j
    @Setter
    @Getter
    @ToString
    @Accessors(chain = true)
    static class DemoB {
        private String name;

        private Integer age;

        private String remark;
    }

    @Data
    @Builder
    static class DemoC {
        private String name;
    }

    /**
     * 相当于以下注解
     * 1.ToString
     * 2.EqualsAndHashCode--可以使用字段为该类生成 Equals 和 HashCode 方法
     * 3.Getter
     * 4.非final字段Setter
     * 5.RequiredArgsConstructor--为每个需要特殊处理的字段生成一个带有对应参数的构造函数，比如final和被@NonNull注解的字段
     */
    @Test
    public void demoA() {
        DemoA demoA = new DemoA();
        demoA.setName("123");

        String name = demoA.getName();
        System.err.println("name = " + name);

        System.err.println("toString：" + demoA);

        System.err.println("hashCode：" + demoA.hashCode());

        System.err.println("equals：" + demoA.equals(new DemoA()));

        System.err.println("canEqual：" + demoA.canEqual(new DemoA()));
    }

    /**
     * NoArgsConstructor：无参的构造方法
     * AllArgsConstructor：所有字段的构造方法
     * Slf4j：private static final Logger log = LoggerFactory.getLogger(xx.class);
     * <p>
     * Accessors:
     * 1）该注解主要作用是：当属性字段在生成 getter 和 setter 方法时，做一些相关的设置
     * 2）当它可作用于类上时，修饰类中所有字段，当作用于具体字段时，只对该字段有效
     * <p>
     * fluent 属性
     * 不写默认为false，当该值为true时，对应字段的getter方法前面就没有get，setter方法就不会有set
     * <p>
     * chain 属性
     * 不写默认为false，当该值为true时，对应字段的setter方法调用后，会返回当前对象
     * <p>
     * prefix 属性
     * 该属性是一个字符串数组，当该数组有值时，表示忽略字段中对应的前缀，生成对应的getter和 setter方法
     */
    @Test
    public void demoB() {
        DemoB demoB = new DemoB().setAge(1).setName("wzh").setRemark("ok");
        System.err.println("demoB = " + demoB.toString());
    }

    /**
     * Builder开启建造者模式
     */
    @Test
    public void demoC() {
        DemoC demoC = DemoC.builder().name("wzh").build();
        System.err.println("demoC = " + demoC);
    }


}
