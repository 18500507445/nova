package com.nova.tools.vc.lockseat;


import com.nova.tools.vc.dataresult.DataResult;
import com.nova.tools.vc.enumerate.TicketSystemEnum;
import com.nova.tools.vc.base.AbstractLockSeatBase;
import com.nova.tools.vc.entity.LockSeatBean;
import com.nova.tools.vc.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @description: 处理晨星锁座
 * @author: wangzehui
 * @date: 2019/5/25 15:20
 */
@Service
public class ChenXingLockSeatProcess extends AbstractLockSeatBase {

    public ChenXingLockSeatProcess() {
        super(TicketSystemEnum.CHENXING);
    }

    @Override
    public DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam) {
        LockSeatBean lockSeatBean = new LockSeatBean();
        return DataResult.success(lockSeatBean);
    }
}
