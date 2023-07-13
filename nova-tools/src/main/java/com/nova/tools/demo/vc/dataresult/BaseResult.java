package com.nova.tools.demo.vc.dataresult;


import java.io.Serializable;

/**
 * @description:
 * @author: vc
 * @date: 2019/8/27 19:10
 */

abstract class BaseResult implements Serializable {
    private static final long serialVersionUID = -5308103810673743579L;

    BaseResult() {

    }

    public abstract Status getStatus();

}
