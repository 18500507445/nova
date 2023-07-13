package com.nova.tools.demo.vc.lockseat;


import com.nova.tools.demo.vc.base.AbstractLockSeatBase;
import com.nova.tools.demo.vc.dataresult.DataResult;
import com.nova.tools.demo.vc.enumerate.TicketSystemEnum;
import com.nova.tools.demo.vc.entity.LockSeatBean;
import com.nova.tools.demo.vc.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @description: 处理满天星锁座
 * @author: wzh
 * @date: 2019/5/25 15:19
 */
@Service
public class CMTSLockSeatProcess extends AbstractLockSeatBase {

    public CMTSLockSeatProcess() {
        super(TicketSystemEnum.CMTS);
    }


    @Override
    public DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam) {
        return null;
    }
}
