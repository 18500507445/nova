package com.nova.tools.demo.vc.base;


import com.nova.tools.demo.vc.dataresult.DataResult;
import com.nova.tools.demo.vc.enumerate.TicketSystemEnum;
import com.nova.tools.demo.vc.entity.LockSeatBean;
import com.nova.tools.demo.vc.entity.LockSeatParamBean;

/**
 * @description: 锁座抽象类
 * @author: wzh
 * @date: 2019/5/25 11:40
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
