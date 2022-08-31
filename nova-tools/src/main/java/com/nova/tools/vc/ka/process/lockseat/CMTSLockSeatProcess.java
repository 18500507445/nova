package com.nova.tools.vc.ka.process.lockseat;


import com.nova.tools.utils.dataresult.DataResult;
import com.nova.tools.utils.enumerate.TicketSystemEnum;
import com.nova.tools.vc.ka.process.base.AbstractLockSeatBase;
import com.nova.tools.vc.ka.process.entity.LockSeatBean;
import com.nova.tools.vc.ka.process.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @Description: 处理满天星锁座
 * @Author: wangzehui
 * @Date: 2019/5/25 15:19
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
