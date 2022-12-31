package com.nova.spring.config;

import com.nova.spring.entity.Card;
import com.nova.spring.entity.People;
import org.springframework.context.annotation.*;

/**
 * @description: 自动化配置
 * @author: wzh
 * @date: 2022/12/29 17:21
 */
@EnableAspectJAutoProxy
@ComponentScan("com.nova.spring")
@Configuration
public class MainConfiguration {

    /**
     * 默认单例
     *
     * @return
     */
    @Bean
    public Card card() {
        return new Card();
    }

    /**
     * 原型模式 @Scope("prototype")
     *
     * @return
     */
    @Bean
    @Scope("prototype")
    public People people() {
        People people = new People();
        people.setName("张三");
        people.setAge(18);
        return people;
    }

}
