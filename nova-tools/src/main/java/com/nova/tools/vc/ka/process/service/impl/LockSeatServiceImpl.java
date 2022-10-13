package com.nova.tools.vc.ka.process.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.nova.tools.utils.dataresult.DataResult;
import com.nova.tools.utils.enumerate.TicketSystemEnum;
import com.nova.tools.vc.ka.process.base.AbstractLockSeatBase;
import com.nova.tools.vc.ka.process.entity.LockSeatBean;
import com.nova.tools.vc.ka.process.entity.LockSeatParamBean;
import com.nova.tools.vc.ka.process.lockseat.CMTSLockSeatProcess;
import com.nova.tools.vc.ka.process.lockseat.ChenXingLockSeatProcess;
import com.nova.tools.vc.ka.process.lockseat.DingXinLockSeatProcess;
import com.nova.tools.vc.ka.process.service.LockSeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/5/25 16:59
 */
@Service
@Slf4j
public class LockSeatServiceImpl implements LockSeatService {
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate restTemplate;

    @Autowired
    private ChenXingLockSeatProcess chenXingLockSeatProcess;

    @Autowired
    private CMTSLockSeatProcess cmtsLockSeatProcess;

    @Autowired
    private DingXinLockSeatProcess dingXinLockSeatProcess;

    @Override
    public DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam) {
        log.warn("处理Ka-c端锁座业务请求参数" + JSONObject.toJSONString(lockSeatParam));
        LockSeatBean data = new LockSeatBean();
        try {
            TicketSystemEnum ticketCode = TicketSystemEnum.getTicketSystemEnum(lockSeatParam.getCType());
            HashMap<TicketSystemEnum, AbstractLockSeatBase> map = new HashMap<>(16);
            //todo
            map.put(TicketSystemEnum.CHENXING, chenXingLockSeatProcess);
            map.put(TicketSystemEnum.CMTS, cmtsLockSeatProcess);
            map.put(TicketSystemEnum.DINGXIN, dingXinLockSeatProcess);
            AbstractLockSeatBase lockSeatBase = map.get(ticketCode);
            DataResult<LockSeatBean> lockSeatResult = lockSeatBase.processLockSeat(lockSeatParam);
            if (lockSeatResult.isSuccess()) {
                //todo 处理
                data.setOrderNo(lockSeatResult.getResult().getOrderNo());
                data.setLockCode(lockSeatResult.getResult().getLockCode());
                return DataResult.success(data);
            } else {
                return DataResult.failed(-1, "处理Ka-c端锁座业务失败");
            }
        } catch (Exception e) {
            log.error("处理Ka-c端锁座业务失败", e);
            return DataResult.failed(-1, "处理Ka-c端锁座业务失败");
        }
    }
}
