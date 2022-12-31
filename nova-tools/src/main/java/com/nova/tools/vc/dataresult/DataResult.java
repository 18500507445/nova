package com.nova.tools.vc.dataresult;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2019/8/27 19:10
 */

public class DataResult<T> extends BaseResult implements Serializable {
    private static final long serialVersionUID = 7179573567777020185L;
    /**
     * 解析协议状态码
     */
    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Status status = new Status(1, "");
    /**
     * 返回Result
     */
    @JsonProperty("result")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    /**
     * 附加属性/扩展属性
     */
    @JsonProperty("attachment")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> attachment;

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static DataResult<Object> success() {
        return new DataResult<Object>();
    }

    public static <T> DataResult<T> success(T result) {
        DataResult<T> dataResult = new DataResult<T>();
        dataResult.setResult(result);
        return dataResult;
    }

    public static <T> DataResult<List<T>> success(List<T> result) {
        DataResult<List<T>> dataResult = new DataResult<List<T>>();
        dataResult.setResult(result);
        return dataResult;
    }

    public static <T> DataResult<T> failed(int statusCode, String statusReason) {
        DataResult<T> dataResult = new DataResult<T>();
        dataResult.setStatus(new Status(statusCode, statusReason));
        return dataResult;
    }

    @SuppressWarnings("unchecked")
    public DataResult<Map<String, Object>> addResult(String key, Object value) {
        Map<String, Object> result = (Map<String, Object>) this.getResult();
        result.put(key, value);
        return (DataResult<Map<String, Object>>) this;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return getStatus().getStatusCode() == 1;
    }

    /**
     * 接口返回错误
     */
    @JsonIgnore
    public boolean isFailed() {
        return !isSuccess();
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public DataResult<T> addAttachment(String key, Object value) {
        if (attachment == null) {
            this.attachment = new HashMap<>(16);
        }
        this.attachment.put(key, value);
        return this;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
