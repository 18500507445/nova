package com.nova.pay;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.enums.PayWayEnum;
import com.nova.pay.strategy.PayStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayApplicationTest {

    @Autowired
    private PayStrategy payStrategy;

    @Test
    public void testPay() {
        int payWay = 1;
        PayWayEnum payWayEnum = PayWayEnum.valuesOf(payWay);
        AjaxResult result = payStrategy.pay(payWayEnum, new PayParam());
        System.out.println(JSONUtil.toJsonStr(result));
    }
}