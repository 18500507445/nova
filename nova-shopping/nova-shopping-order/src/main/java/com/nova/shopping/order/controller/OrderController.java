package com.nova.shopping.order.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.nova.shopping.common.annotation.AccessLimit;
import com.nova.shopping.common.config.RedisService;
import com.nova.shopping.common.constant.BaseController;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.common.constant.dto.BaseReqDTO;
import com.nova.shopping.common.constant.dto.OrderParam;
import com.nova.shopping.common.constant.result.AjaxResult;
import com.nova.shopping.common.constant.result.RespResultCode;
import com.nova.shopping.order.entity.MyGoods;
import com.nova.shopping.order.entity.MySeckillOrder;
import com.nova.shopping.order.entity.MyUser;
import com.nova.shopping.order.service.MyGoodsService;
import com.nova.shopping.order.service.MyOrderService;
import com.nova.shopping.order.service.MySeckillOrderService;
import com.nova.shopping.order.service.MyUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 订单controller
 * @author: wzh
 * @date: 2023/4/15 15:55
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class OrderController extends BaseController {

    private final RedisService redisService;

    private final RedisScript<Long> script;

    private final MyGoodsService myGoodsService;

    private final MyUserService myUserService;

    private final MyOrderService myOrderService;

    private final MySeckillOrderService mySeckillOrderService;

    //private final MyPayConfigService myPayConfigService;

    @Value("${spring.datasource.druid.maxActive}")
    private final Long maxActive = 0L;

    /**
     * 获取订单id
     */
    @AccessLimit(seconds = 1, maxCount = 1)
    @PostMapping("getOrderId")
    public AjaxResult getOrderId(OrderParam param) {
        Map<String, String> result = new HashMap<>(16);
        log.info("getOrderId请求入参：{}", JSONUtil.toJsonStr(param));
        String sid = param.getSid();
        String source = param.getSource();
        String newVersion = param.getNewVersion();
        Integer payWay = param.getPayWay();
        String userName = param.getUserName();
        String totalAmount = param.getTotalAmount();
        String clientType = param.getClientType();
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(source, sid, clientType, userName, totalAmount, payWay)) {
            return AjaxResult.error("1000", "缺少必要参数", null);
        }
        try {
            Map<String, String> paramsMap = new HashMap<>(16);
            paramsMap.put("function", "getOrderId");
            paramsMap.put("source", source);
            paramsMap.put("sid", sid);
            paramsMap.put("newVersion", newVersion);
            paramsMap.put("userName", userName);
            paramsMap.put("totalAmount", "totalAmount");
            //根据clientType判断充值类型或者汇率
            String productId = sid + "|pay" + sid + "|" + clientType;
            paramsMap.put("productId", productId);
            paramsMap.put("tradeId", "");
            //固定获取支付配置
            if (ObjectUtil.isNotNull(payConfigId)) {
                //MyPayConfig payConfig = myPayConfigService.selectMyPayConfigById(payConfigId);
                //if (ObjectUtil.isNotNull(payConfig)) {
                //    result.put("payConfigId", String.valueOf(payConfigId));
                //}
            } else {
                //随机一个支付配置
                //MyPayConfig randomConfig = myPayConfigService.getRandomConfigData(source, sid, String.valueOf(payWay));
                //if (ObjectUtil.isNotNull(randomConfig)) {
                //    result.put("payConfigId", String.valueOf(randomConfig.getId()));
                //}
            }
            String orderId = myOrderService.getOrderId(paramsMap);
            result.put("orderId", orderId);
            result.put("productId", productId);
        } catch (Exception e) {
            log.error("getOrderId异常：{}", e.getMessage());
        }
        return AjaxResult.success(result);
    }

    @AccessLimit(seconds = 1, maxCount = 1)
    @PostMapping("doBuy")
    public AjaxResult doBuy(BaseReqDTO baseReqDTO) {
        log.info("doBuy请求入参：{}", JSONUtil.toJsonStr(baseReqDTO));
        final Long userId = baseReqDTO.getUserId();
        final Long goodsId = baseReqDTO.getGoodsId();
        if (ObjectUtil.isAllEmpty(userId, goodsId)) {
            return AjaxResult.error(RespResultCode.ERR_PARAM_NOT_LEGAL);
        }

        //基本信息校验
        if (!checkBase(userId, goodsId)) {
            AjaxResult.error(RespResultCode.BUY_NOT_ESTABLISHED);
        }

        //3秒内用户请求总数不超过数据库最大连接数 4/5
        String userTotalRequestKey = Constants.REDIS_KEY + MyUser.USER_TOTAL_REQUEST;
        redisService.incr(userTotalRequestKey, 1L);
        int userTotalRequest = Convert.toInt(redisService.get(userTotalRequestKey), 0);
        if (userTotalRequest >= maxActive) {
            return AjaxResult.error(RespResultCode.REPEATED_BUSY);
        }
        redisService.expire(userTotalRequestKey, 3L);

        //缓存预热，然后lua脚本保证查询库存和扣减原子性
        final Long stock = redisService.execute(script, Convert.toStr(goodsId));
        if (stock < 0) {
            return AjaxResult.error(RespResultCode.REPEATED_BUSY);
        }
        /**
         * 几种方案
         * 1.mysql 乐观锁，
         *
         * 2.mysql 悲观锁
         * 1.
         * 2.
         */
        return AjaxResult.success(RespResultCode.ORDER_QUEUING);
    }


    /**
     * 校验用户信息，商品信息，是否参与过秒杀
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public boolean checkBase(Long userId, Long goodsId) {
        MyUser myUser = myUserService.queryById(userId);
        if (ObjectUtil.isNull(myUser) || 1 == myUser.getStatus()) {
            return false;
        }
        MyGoods myGoods = myGoodsService.queryById(goodsId);
        if (ObjectUtil.isNull(myGoods) || 1 == myGoods.getStatus() || myGoods.getStock() <= 0) {
            return false;
        }
        MySeckillOrder seckillOrder = mySeckillOrderService.queryById(userId, goodsId);
        return ObjectUtil.isNotNull(seckillOrder);
    }

}
