package com.nova.book.design.action.strategy;

import com.nova.book.design.action.strategy.coupon.CouponStrategyContext;
import com.nova.book.design.action.strategy.coupon.FullDiscountCoupon;
import com.nova.book.design.action.strategy.coupon.service.DiscountCouponImpl;
import com.nova.book.design.action.strategy.coupon.service.FullDiscountCouponImpl;
import com.nova.book.design.action.strategy.pay.entity.Order;
import com.nova.book.design.action.strategy.pay.enums.PayType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @description: 策略模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void testPay() {
        //省略把商品添加到购物车，再从购物车下单
        //直接从点单开始
        Order order = new Order("1", "20180311001000009", 324.45);

        //开始支付，选择微信支付、支付宝、银联卡、京东白条、财付通
        //每个渠道它支付的具体算法是不一样的
        //基本算法固定的

        //这个值是在支付的时候才决定用哪个值
        System.err.println(order.pay(PayType.WECHAT_PAY));
    }

    @Test
    public void testDiscount() {
        // 折扣券 0.9折
        CouponStrategyContext<BigDecimal> couponStrategyContext = new CouponStrategyContext<>(new DiscountCouponImpl());
        BigDecimal calPrice = couponStrategyContext.calPrice(BigDecimal.valueOf(100), BigDecimal.valueOf(0.9));
        System.err.println("使用折扣券后：" + calPrice);
    }

    @Test
    public void testFullDiscount() {
        // 满减券 满100减10
        CouponStrategyContext<FullDiscountCoupon> couponStrategyContext = new CouponStrategyContext<>(new FullDiscountCouponImpl());
        BigDecimal calPrice = couponStrategyContext.calPrice(
                BigDecimal.valueOf(200),
                FullDiscountCoupon.builder()
                        .thresholdPrice(BigDecimal.valueOf(100))
                        .discountPrice(BigDecimal.valueOf(10))
                        .build()
        );
        System.err.println("使用满减券后：" + calPrice);
    }
}
