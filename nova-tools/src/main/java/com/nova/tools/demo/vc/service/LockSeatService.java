package com.nova.tools.demo.vc.service;


import com.nova.tools.demo.vc.dataresult.DataResult;
import com.nova.tools.demo.vc.entity.LockSeatBean;
import com.nova.tools.demo.vc.entity.LockSeatParamBean;

/**
 * @description:
 * @author: wzh
 * @date: 2019/5/25 17:19
 */

public interface LockSeatService {

    /**
     * 处理锁座
     *
     * @param lockSeatParam
     * @return
     */
    DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam);
}
