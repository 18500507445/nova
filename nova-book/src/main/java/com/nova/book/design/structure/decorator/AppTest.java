package com.nova.book.design.structure.decorator;

import com.nova.book.design.structure.decorator.coupon.DiscountCouponCalDecorator;
import com.nova.book.design.structure.decorator.coupon.FullDiscountCouponCalDecorator;
import com.nova.book.design.structure.decorator.coupon.OrderCal;
import com.nova.book.design.structure.decorator.coupon.model.Product;
import com.nova.book.design.structure.decorator.people.BikeDecorator;
import com.nova.book.design.structure.decorator.people.CarDecorator;
import com.nova.book.design.structure.decorator.people.Person;
import com.nova.book.design.structure.decorator.people.SimplePerson;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @description: 装饰器模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {


    @Test
    public void testPeople(){
        // 起初 这个人只会走路...
        Person person = new SimplePerson();
        // 现在给他装饰下，配个单车
        BikeDecorator bike = new BikeDecorator(person);
        // 再装饰下，配个小汽车
        CarDecorator car = new CarDecorator(bike);
        // 让我们来看看现在这个人都有些啥了...
        car.run();
    }

    @Test
    public void testCoupon(){
        ArrayList<Product> productList = Lists.newArrayList(
                Product.builder().name("台灯").price(BigDecimal.valueOf(100)).num(1).build(),
                Product.builder().name("钢笔").price(BigDecimal.valueOf(9.9)).num(2).build(),
                Product.builder().name("笔记本").price(BigDecimal.valueOf(20)).num(3).build()
        );

        OrderCal orderCal = new OrderCal(productList);

        // 输出商品信息
        productList.forEach(e -> System.out.println(String.format("*** [%s  %sx%s]", e.getName(), e.getPrice(), e.getNum())));
        System.out.println("总金额: " + orderCal.price());

        // 一张折扣券（9折） & 一张满减券（满100减10）  -- 真实业务中，这里使用的优惠券是前端提交的，即动态数据 那么如何做到动态计算呢？？？
        FullDiscountCouponCalDecorator decorator = new FullDiscountCouponCalDecorator(
                new DiscountCouponCalDecorator(orderCal, BigDecimal.valueOf(0.9)),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10)
        );
        System.err.println("优惠后金额: " + decorator.price());
    }
}