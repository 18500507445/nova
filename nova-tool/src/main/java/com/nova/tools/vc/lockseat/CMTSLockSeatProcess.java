package com.nova.tools.vc.lockseat;


import com.nova.tools.vc.dataresult.DataResult;
import com.nova.tools.vc.enumerate.TicketSystemEnum;
import com.nova.tools.vc.base.AbstractLockSeatBase;
import com.nova.tools.vc.entity.LockSeatBean;
import com.nova.tools.vc.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @description: 处理满天星锁座
 * @author: wangzehui
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
