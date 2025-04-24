package com.nova.shopping.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.order.repository.mapper.SeckillOrderDao;
import com.nova.shopping.order.repository.entity.SeckillOrder;
import com.nova.shopping.order.service.SeckillOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wzh
 * @description: 秒杀订单表(MySeckillOrder)表服务实现类
 * @date: 2023-04-15 15:53
 */
@Service("mySeckillOrderService")
@RequiredArgsConstructor
public class SeckillOrderServiceImpl implements SeckillOrderService {

    private final RedisService redisService;

    private final SeckillOrderDao seckillOrderDao;

    private static final String SECKILL_ORDER = Constants.REDIS_KEY + SeckillOrder.SECKILL_ORDER;

    /**
     * 通过ID查询单条数据
     *
     * @param userId  用户id
     * @param goodsId 商品id
     */
    @Override
    public SeckillOrder queryById(Long userId, Long goodsId) {
        Object o = redisService.get(SECKILL_ORDER + userId + goodsId);
        SeckillOrder seckillOrder;
        if (ObjectUtil.isNull(o)) {
            seckillOrder = seckillOrderDao.queryById(userId, goodsId);
            if (ObjectUtil.isNotNull(seckillOrder)) {
                redisService.set(SECKILL_ORDER + userId + goodsId, seckillOrder, 3600L);
            }
        } else {
            seckillOrder = JSONObject.parseObject(o.toString(), SeckillOrder.class);
        }
        return seckillOrder;
    }

    /**
     * 分页查询list
     *
     * @param seckillOrder 筛选条件
     * @return 查询结果
     */
    @Override
    public List<SeckillOrder> queryList(SeckillOrder seckillOrder) {
        return seckillOrderDao.queryList(seckillOrder);
    }

    /**
     * 新增数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(SeckillOrder seckillOrder) {
        return seckillOrderDao.insert(seckillOrder);
    }

    /**
     * 修改数据
     *
     * @param seckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int update(SeckillOrder seckillOrder) {
        int update = seckillOrderDao.update(seckillOrder);
        if (update > 0) {
            refreshRedis(seckillOrder.getUserId(), seckillOrder.getGoodsId());
        }
        return update;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return seckillOrderDao.deleteById(id) > 0;
    }

    /**
     * 刷新缓存
     *
     * @param userId  用户id
     * @param goodsId 商品id
     */
    public void refreshRedis(Long userId, Long goodsId) {
        redisService.del(SECKILL_ORDER + userId + goodsId);
    }
}
