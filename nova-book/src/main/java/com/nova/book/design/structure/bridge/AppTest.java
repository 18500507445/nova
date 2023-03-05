package com.nova.book.design.structure.bridge;


import com.nova.book.design.structure.bridge.channel.Payment;
import com.nova.book.design.structure.bridge.channel.WxPay;
import com.nova.book.design.structure.bridge.channel.AliPay;
import com.nova.book.design.structure.bridge.mode.CypherPayMode;
import com.nova.book.design.structure.bridge.mode.FacePayMode;
import org.junit.jupiter.api.Test;

/**
 * @description: 桥接模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void test() throws Exception {
        Payment wxPay = new WxPay(new FacePayMode());
        wxPay.transfer();

        Payment zfbPay = new AliPay(new CypherPayMode());
        zfbPay.transfer();
    }

}