import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author: wzh
 * @description Sms4j测试类
 * @date: 2023/09/30 16:31
 */
@Slf4j(topic = "Sms4jTest")
@SpringBootTest
public class Sms4jTest {

    @Test
    public void testSend() {
        //在创建完SmsBlend实例后，再未手动调用注销的情况下框架会持有该实例，可以直接通过指定configId来获取想要的配置
        //如果你想使用负载均衡形式获取实例，只要使用getSmsBlend的无参重载方法即可，如果你仅有一个配置，也可以使用该方法
        SmsBlend smsBlend = SmsFactory.getSmsBlend("在配置中定义的configId");

        //普通发送
        SmsResponse smsResponse = smsBlend.sendMessage("18888888888", "123");

        //模板发送
        SmsResponse templateSend = smsBlend.sendMessage("18888888888", "templateId", new LinkedHashMap<>());

        //群发
        SmsResponse massSend = smsBlend.massTexting(new ArrayList<>(), "msg");

        //异步短信
        smsBlend.sendMessageAsync("18888888888", "123456", e -> log.info(e.toString()));

    }

    /**
     * sms4j自带的工具类
     */
    @Test
    public void testUtils() {
        //获取一个指定长度的随机字符串，包含数字和大小写字母，不包含符号和空格
        SmsUtils.getRandomString(6);

        //获取一个六位长度的随机字符串，包含数字和大小写字母，不包含符号和空格
        SmsUtils.getRandomString();

        //获取一个指定长度的随机纯数字组成的字符串
        SmsUtils.getRandomInt(4);
    }


}
