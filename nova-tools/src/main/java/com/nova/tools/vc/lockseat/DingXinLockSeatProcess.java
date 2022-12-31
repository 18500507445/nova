package com.nova.tools.vc.lockseat;


import com.nova.tools.vc.dataresult.DataResult;
import com.nova.tools.vc.enumerate.TicketSystemEnum;
import com.nova.tools.vc.base.AbstractLockSeatBase;
import com.nova.tools.vc.entity.LockSeatBean;
import com.nova.tools.vc.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @description: 处理鼎新
 * @author: wzh
 * @date: 2019/5/25 15:22
 */
@Service
public class DingXinLockSeatProcess extends AbstractLockSeatBase {

    public DingXinLockSeatProcess() {
        super(TicketSystemEnum.DINGXIN);
    }


    @Override
    public DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam) {
        return null;
    }
}
