package com.nova.common.utils.common;

import cn.hutool.core.lang.ConsistentHash;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * @author: wzh
 * @description: //todo 一致性hash工具类，后续拆大key用
 * @date: 2024/01/08 16:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HashUtils {

    //------------------------------商品模块一致性hash解决------------------------------

    private static final int PRODUCT_NUM_KEYS = 16;

    private static final String[] PRODUCT_KEYS = {
            "key0", "key1", "key2", "key3", "key4", "key5", "key6", "key7",
            "key8", "key9", "key10", "key11", "key12", "key13", "key14", "key15"
    };

    // 构建一致性哈希环
    public static final ConsistentHash<String> CONSISTENT_HASH = new ConsistentHash<>(PRODUCT_NUM_KEYS, Arrays.asList(PRODUCT_KEYS));

    public static String getProductKey(String skuId) {
        return CONSISTENT_HASH.get(skuId);
    }

    public static void main(String[] args) {
        String productKey = getProductKey("123");
        System.out.println("productKey = " + productKey);
    }
}
