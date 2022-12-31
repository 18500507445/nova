package com.nova.tools.vc.dataresult;

import java.io.Serializable;

/**
 * @author: wzh
 */
public class ObjectParam implements Serializable {
    private static final long serialVersionUID = -4759249614246684897L;
    /**
     * 状态
     */
    private int status = -1;
    /**
     * 消息
     */
    private String message;


    private String errorMessage;

    private String requestStr;

    private String responseStr;

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
