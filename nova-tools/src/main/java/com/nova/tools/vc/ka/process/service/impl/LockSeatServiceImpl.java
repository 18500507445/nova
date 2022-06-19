package com.nova.tools.vc.ka.process.service.impl;//package kac.trade.process.service.impl;
//
//
//import cn.vcfilm.sts.framework.code.ProcessCode;
//import cn.vcfilm.sts.opencommon.domian.DataResult;
//import com.alibaba.fastjson.JSONObject;
//
//import kac.trade.process.base.AbstractLockSeatBase;
//import kac.trade.process.entity.LockSeatBean;
//import kac.trade.process.entity.LockSeatParamBean;
//import kac.trade.process.lockseat.CMTSLockSeatProcess;
//import kac.trade.process.lockseat.ChenXingLockSeatProcess;
//import kac.trade.process.lockseat.DingXinLockSeatProcess;
//import kac.trade.process.service.LockSeatService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.http.HttpHeaders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import org.springframework.stereotype.Service;
//import utils.TicketSystemEnum;
//
//
//import java.util.HashMap;
//
///**
// * @Description:
// * @Author: wangzehui
// * @Date: 2019/5/25 16:59
// */
//@Service
//@Slf4j
//public class LockSeatServiceImpl implements LockSeatService {
//    @Autowired
//    @Qualifier(value = "remoteRestTemplate")
//    protected RestTemplate restTemplate;
//
//    @Autowired
//    private TenantApi tenantApi;
//
//    @Autowired
//    private ChenXingLockSeatProcess chenXingLockSeatProcess;
//
//    @Autowired
//    private CMTSLockSeatProcess cmtsLockSeatProcess;
//
//    @Autowired
//    private DingXinLockSeatProcess dingXinLockSeatProcess;
//
//    @Override
//    public DataResult<LockSeatBean> processLockSeat(LockSeatParamBean lockSeatParam) {
//        //获取token
//        KaCodeDto kaCodeDto = new KaCodeDto();
//        kaCodeDto.setKaCode(Integer.valueOf(lockSeatParam.getKaCode()));
//        DataResult<String> accessToken = tenantApi.getAccessToken(kaCodeDto);
//        if (accessToken.isFailed()) {
//            return DataResult.failed(ProcessCode.INTERNAL_ERROR, "token为空");
//        }
//        //调中台
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add(CommonConstant.AUTH_CODE_HEADER_KEY, accessToken.getResult());
//        //放数据
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("", null);
//        HttpEntity<Object> entity = new HttpEntity<>(jsonObject, headers);
//        ResponseEntity<DataResult> exchange = restTemplate.exchange("todo" + OptimusUrlConstant.AUTH_AREA_ALL_ADDRESS, HttpMethod.POST, entity, DataResult.class);
//
//        log.warn("处理Ka-c端锁座业务请求参数" + JSONObject.toJSONString(lockSeatParam));
//        LockSeatBean data = new LockSeatBean();
//        try {
//            TicketSystemEnum ticketCode = TicketSystemEnum.getTicketSystemEnum(lockSeatParam.getCType());
//            HashMap<TicketSystemEnum, AbstractLockSeatBase> map = new HashMap<>(16);
//            //todo
//            map.put(TicketSystemEnum.CHENXING, chenXingLockSeatProcess);
//            map.put(TicketSystemEnum.CMTS, cmtsLockSeatProcess);
//            map.put(TicketSystemEnum.DINGXIN, dingXinLockSeatProcess);
//            AbstractLockSeatBase lockSeatBase = map.get(ticketCode);
//            DataResult<LockSeatBean> lockSeatResult = lockSeatBase.processLockSeat(lockSeatParam);
//            if (lockSeatResult.isSuccess()) {
//                //todo 处理
//                data.setOrderNo(lockSeatResult.getResult().getOrderNo());
//                data.setLockCode(lockSeatResult.getResult().getLockCode());
//                return DataResult.success(data);
//            } else {
//                return DataResult.failed(ProcessCode.INTERNAL_ERROR, "处理Ka-c端锁座业务失败");
//            }
//        } catch (Exception e) {
//            log.error("处理Ka-c端锁座业务失败", e);
//            return DataResult.failed(ProcessCode.INTERNAL_ERROR, "处理Ka-c端锁座业务失败");
//        }
//    }
//}
