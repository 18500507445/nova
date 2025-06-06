package com.nova.tools.common;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.nlf.calendar.Holiday;
import com.nlf.calendar.util.HolidayUtil;
import com.nova.common.utils.common.LinuxUtils;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: wzh
 * @description: 通用测试类
 * @date: 2023/07/24 14:43
 */
@Slf4j(topic = "commonTest")
@SpringBootTest
public class CommonTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    /**
     * 加密解密测试
     */
    @Test
    public void jasyptTest() {
        // 加密
        System.err.println(stringEncryptor.encrypt("root"));
        // 解密
        System.err.println(stringEncryptor.decrypt("4+fSPJL3wq+pZm9IVnD9ssbuH0qW1vky4Kdq0EO5vOe1LdTl1+DpnjrXImMb5ef5"));
    }

    @Data
    @EqualsAndHashCode
    static class skuDTO {
        private String skuId = "";
        private String cateId = "";
        private Integer poolType;
    }

    /**
     * 计算对象占用内存大小
     */
    @Test
    public void testObjectByteSize() {
        List<Object> as = new ArrayList<>();
        for (int i = 0; i < 5000000; i++) {
            skuDTO skuDTO = new skuDTO();
            skuDTO.setSkuId(RandomUtil.randomNumbers(12));
            skuDTO.setCateId(RandomUtil.randomNumbers(8));
            skuDTO.setPoolType(1);
            as.add(skuDTO);
        }
        System.err.println("占用：" + ObjectSizeCalculator.getObjectSize(as) / 1024 / 1024 + "mb");
    }

    /**
     * 分配空间 -Xmx120m -Xms120m -XX:SurvivorRatio=8 -XX:NewRatio=2
     * 本地打开VisualVM工具查看分配情况
     */
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            final int _100kb = 1024 * 1024;
            byte[] arr = new byte[_100kb];
            list.add(arr);
            ThreadUtil.sleep(30, TimeUnit.MINUTES);
        }
    }

    /**
     * grep console测试
     */
    @Test
    public void testLog() {
        log.info("记录:{}", "infoLog");
        log.warn("警告:{}", "warnLog");
        log.error("异常:{}", "errorLog");
    }

    /**
     * 测试linux命令
     */
    @Test
    public void getIp() {
        String ip = LinuxUtils.getInternetIp("curl cip.cc");
        System.err.println("ip = " + ip);
    }

    //

    /**
     * @description: 判断是否法定假假日、调休
     * @see <a href="https://6tail.cn/calendar/api.html#holiday-util.about.html">官网</a>
     */
    @Test
    public void holiday() {
        Holiday holiday = HolidayUtil.getHoliday(2024, 9, 16);
        Console.log("日期：" + holiday.getDay());
        Console.log("是否调休：" + holiday.isWork());
        Console.log("假期名称：" + holiday.getName());
        Console.log("关联节日：" + holiday.getTarget());
    }

    //获取所有节日
    @Test
    public void holidays() {
        List<Holiday> holidays = HolidayUtil.getHolidays(2024);
        String jsonStr = JSONUtil.toJsonStr(holidays);
        Console.log("holidays：" + jsonStr);
    }
}
