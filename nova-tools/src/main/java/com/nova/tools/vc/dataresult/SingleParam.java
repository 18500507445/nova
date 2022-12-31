package com.nova.tools.vc.dataresult;

import java.io.Serializable;

/**
 * @author: wzh
 * 单个对象返回对象
 */
public class SingleParam<T> extends ObjectParam implements Serializable {

    private static final long serialVersionUID = -6558073907202141925L;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
