package com.nova.database.binlog4j.bean;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author: wzh
 * @description: 测试binlog4j测试类
 * @date: 2024/08/14 09:44
 */
@Data
public class BinLog4j {

    private Long id;

    private String skuId;

    private Integer status;

    private String json;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
