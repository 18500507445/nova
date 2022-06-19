package com.nova.tools.vc.ka.process.base;//package kac.trade.process.base;
//
//
//import cn.vcfilm.sts.opencommon.domian.DataResult;
//import kac.trade.process.entity.LockSeatBean;
//import kac.trade.process.entity.LockSeatParamBean;
//import utils.TicketSystemEnum;
//
///**
// * @Description: 锁座抽象类
// * @Author: wangzehui
// * @Date: 2019/5/25 11:40
// */
//
//public abstract class AbstractLockSeatBase {
//    //系统商类型
//    private TicketSystemEnum ticketSystemEnum;
//
//    public abstract DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam);
//
//    public TicketSystemEnum getTicketSystemEnum() {
//        return ticketSystemEnum;
//    }
//
//    public AbstractLockSeatBase(TicketSystemEnum ticketSystemEnum) {
//        this.ticketSystemEnum = ticketSystemEnum;
//    }
//}
