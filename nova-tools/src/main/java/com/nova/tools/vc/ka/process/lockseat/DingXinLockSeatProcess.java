package com.nova.tools.vc.ka.process.lockseat;


import com.nova.tools.utils.dataresult.DataResult;
import com.nova.tools.utils.enumerate.TicketSystemEnum;
import com.nova.tools.vc.ka.process.base.AbstractLockSeatBase;
import com.nova.tools.vc.ka.process.entity.LockSeatBean;
import com.nova.tools.vc.ka.process.entity.LockSeatParamBean;
import org.springframework.stereotype.Service;

/**
 * @Description: 处理鼎新
 * @Author: wangzehui
 * @Date: 2019/5/25 15:22
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
