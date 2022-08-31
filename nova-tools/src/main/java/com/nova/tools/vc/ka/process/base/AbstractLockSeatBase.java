package com.nova.tools.vc.ka.process.base;


import com.nova.tools.utils.dataresult.DataResult;
import com.nova.tools.utils.enumerate.TicketSystemEnum;
import com.nova.tools.vc.ka.process.entity.LockSeatBean;
import com.nova.tools.vc.ka.process.entity.LockSeatParamBean;

/**
 * @Description: 锁座抽象类
 * @Author: wangzehui
 * @Date: 2019/5/25 11:40
 */

public abstract class AbstractLockSeatBase {

    //系统商类型
    private TicketSystemEnum ticketSystemEnum;

    public abstract DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam);

    public TicketSystemEnum getTicketSystemEnum() {
        return ticketSystemEnum;
    }

    public AbstractLockSeatBase(TicketSystemEnum ticketSystemEnum) {
        this.ticketSystemEnum = ticketSystemEnum;
    }
}
