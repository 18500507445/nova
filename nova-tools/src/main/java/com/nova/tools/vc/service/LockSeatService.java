package com.nova.tools.vc.service;


import com.nova.tools.vc.dataresult.DataResult;
import com.nova.tools.vc.entity.LockSeatBean;
import com.nova.tools.vc.entity.LockSeatParamBean;

/**
 * @description:
 * @author: wangzehui
 * @date: 2019/5/25 17:19
 */

public interface LockSeatService {

    /**
     * 处理锁座
     * @param lockSeatParam
     * @return
     */
    DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam);
}
