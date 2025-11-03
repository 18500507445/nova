package com.nova.mq.rabbit.entity;

import lombok.Data;

import java.util.List;

/**
 * @author: wzh
 * @description:
 * @date: 2025/10/31 17:53
 */
@Data
public class CanalCloudBean {

    private String schema;
    private String entryType;
    private List<DataItem> data;
    private List<Object> before;
    private int sendTs;
    private String sql;
    private int execTs;
    private JdbcType jdbcType;
    private String action;
    private List<Object> pks;
    private DbValType dbValType;
    private int bid;
    private String db;
    private String table;
    private boolean isDdl;

    @Data
    public static class DataItem {
        private String updateTime;
        private String createTime;
        private String json;
        private String skuId;
        private String id;
        private String status;
    }

    @Data
    public static class DbValType {
        private String updateTime;
        private String createTime;
        private String json;
        private String skuId;
        private String id;
        private String status;
    }

    @Data
    public static class JdbcType {
        private String updateTime;
        private String createTime;
        private String json;
        private String skuId;
        private String id;
        private String status;
    }
}
