package com.nova.limit.bucket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 桶子实体类
 * @author: wzh
 * @date: 2023/4/18 20:43
 */
@Data
@Accessors(chain = true)
public class Bucket {

    /**
     * 桶子key
     */
    private String key;

    /**
     * 桶子最大容量
     */
    private int maxCapacity;

    /**
     * 桶子水流，流入速度，单位秒
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
        this.setCapacity(this.maxCapacity);
    }
}
