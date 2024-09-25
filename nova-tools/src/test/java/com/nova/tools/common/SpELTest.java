package com.nova.tools.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author: wzh
 * @description: Spring Expression Language
 * @date: 2024/09/25 09:37
 */
@Slf4j(topic = "SpELTest")
@SpringBootTest
public class SpELTest {

    private static final ExpressionParser parser = new SpelExpressionParser();

    //常量测试类
    @Test
    public void constantTest() {
        String str = parser.parseExpression("'Hello,Literal Expression'").getValue(String.class);
        System.out.println("str = " + str);

        Double number = parser.parseExpression("4.5").getValue(Double.class);
        System.out.println("number = " + number);

        Boolean bool = parser.parseExpression("true").getValue(Boolean.class);
        System.out.println("bool = " + bool);

        Object obj = parser.parseExpression("null").getValue();
        System.out.println("obj = " + obj);
    }

    //方法表达式 1.java原生 2.工具类
    @Test
    public void methodTest() {
        String subString = parser.parseExpression("'Hello,SpEL'.substring(0,5)").getValue(String.class);
        System.out.println("subString = " + subString);

        String idCard = parser.parseExpression("#{T(com.nova.common.utils.random.RandomUtils).randomIdCard()}",
                new TemplateParserContext()).getValue(String.class);
        System.out.println("idCard = " + idCard);
    }

    //运算符表达式
    @Test
    public void operatorsTest() {
        Boolean result = parser.parseExpression("2 < 5").getValue(Boolean.class);
        System.out.println("2 < 5 : " + result);

        result = parser.parseExpression("1 instanceof T(Integer)").getValue(Boolean.class);
        System.out.println("1 instanceof T(Integer) : " + result);
    }

    //类表达式
    @Test
    public void objTest() {
        Class<?> dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
        System.out.println("dateClass = " + dateClass);
    }

}
