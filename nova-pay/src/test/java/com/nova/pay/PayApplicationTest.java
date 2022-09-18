package com.nova.pay;

import com.nova.common.core.domain.AjaxResult;
import com.nova.pay.entity.param.PayParam;
import com.nova.pay.strategy.PayTypeEnum;
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
        AjaxResult result = payStrategy.pay(PayTypeEnum.WECHAT, new PayParam());
        System.out.println(result);
    }
}
