package com.nova.tools.vc.service;


import com.nova.tools.utils.dataresult.DataResult;
import com.nova.tools.vc.ka.process.entity.LockSeatBean;
import com.nova.tools.vc.ka.process.entity.LockSeatParamBean;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/5/25 17:19
 */

public interface LockSeatService {

    DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam);
}
