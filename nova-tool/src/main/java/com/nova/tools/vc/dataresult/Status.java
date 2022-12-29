package com.nova.tools.vc.dataresult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @description:
 * @author: wangzehui
 * @date: 2019/8/27 19:10
 */
public class Status {

    @JsonProperty("statusCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int statusCode = 0;

    @JsonProperty("statusReason")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusReason;

    public Status() {

    }

    public Status(int statusCode, String statusReason) {
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * 注意该种方式只在是使用序列化框架
     *
     * @param statusCode
     */
    @SuppressWarnings("unused")
    private void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    @Override
    public String toString() {
        return "{\"statusCode\":" + statusCode + ",\"statusReason\":\"" + statusReason + "\"}";
    }
}
