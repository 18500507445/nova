package com.nova.tools;

import cn.hutool.core.util.RandomUtil;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description 通用测试类
 * @date: 2023/07/24 14:43
 */
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
        System.out.println(stringEncryptor.encrypt("root"));
        // 解密
        System.out.println(stringEncryptor.decrypt("4+fSPJL3wq+pZm9IVnD9ssbuH0qW1vky4Kdq0EO5vOe1LdTl1+DpnjrXImMb5ef5"));
    }

    @Data
    @EqualsAndHashCode
    static class skuDTO {
        String skuId = "";
        String cateId = "";
        Integer poolType;
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
        System.out.println("占用：" + ObjectSizeCalculator.getObjectSize(as) / 1024 / 1024 + "mb");
    }

}