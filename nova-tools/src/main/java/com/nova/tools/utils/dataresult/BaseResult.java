package com.nova.tools.utils.dataresult;


import java.io.Serializable;

/**
 * @Description:
 * @Author: vc
 * @Date: 2019/8/27 19:10
 */

abstract class BaseResult implements Serializable {
    private static final long serialVersionUID = -5308103810673743579L;

    BaseResult() {
    }

    public abstract Status getStatus();

}
