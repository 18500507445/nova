package com.nova.limit.bucket;

import lombok.Data;

/**
 * @description: 桶子实体类
 * @author: wzh
 * @date: 2023/4/18 20:43
 */
@Data
public class Bucket {

    /**
     * 桶子key
     */
    private String key;

    /**
     * 桶子最大容量
     */
    private int bucketMaxCapacity;

    /**
     * 桶子水流流入速度，单位秒
     */
    private int putSpeed;

    /**
     * 开始计时时间
     */
    private long startTime;

    /**
     * 桶子容量
     */
    private int capacity;

    /**
     * 创建桶子时容量拉满
     */
    public Bucket() {
        this.setCapacity(this.bucketMaxCapacity);
    }
}
