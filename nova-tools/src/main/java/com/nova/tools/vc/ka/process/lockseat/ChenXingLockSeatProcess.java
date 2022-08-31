package com.nova.tools.vc.ka.process.lockseat;


import com.nova.tools.utils.dataresult.DataResult;
import com.nova.tools.utils.enumerate.TicketSystemEnum;
import com.nova.tools.vc.ka.process.base.AbstractLockSeatBase;
import com.nova.tools.vc.ka.process.entity.LockSeatBean;
import com.nova.tools.vc.ka.process.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @Description: 处理晨星锁座
 * @Author: wangzehui
 * @Date: 2019/5/25 15:20
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
