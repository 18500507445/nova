package com.nova.tools.demo.vc.lockseat;


import com.nova.tools.demo.vc.dataresult.DataResult;
import com.nova.tools.demo.vc.enumerate.TicketSystemEnum;
import com.nova.tools.demo.vc.base.AbstractLockSeatBase;
import com.nova.tools.demo.vc.entity.LockSeatBean;
import com.nova.tools.demo.vc.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @description: 处理晨星锁座
 * @author: wzh
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