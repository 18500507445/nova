package com.nova.shopping.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.config.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.order.dao.MySeckillOrderDao;
import com.nova.shopping.order.entity.MySeckillOrder;
import com.nova.shopping.order.service.MySeckillOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wzh
 * @description: 秒杀订单表(MySeckillOrder)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("mySeckillOrderService")
@RequiredArgsConstructor
public class MySeckillOrderServiceImpl implements MySeckillOrderService {

    private final RedisService redisService;

    private final MySeckillOrderDao mySeckillOrderDao;

    private static final String SECKILL_ORDER = Constants.REDIS_KEY + MySeckillOrder.SECKILL_ORDER;

    /**
     * 通过ID查询单条数据
     *
     * @param userId  用户id
     * @param goodsId 商品id
     */
    @Override
    public MySeckillOrder queryById(Long userId, Long goodsId) {
        Object o = redisService.get(SECKILL_ORDER + userId + goodsId);
        MySeckillOrder seckillOrder;
        if (ObjectUtil.isNull(o)) {
            seckillOrder = mySeckillOrderDao.queryById(userId, goodsId);
            if (ObjectUtil.isNotNull(seckillOrder)) {
                redisService.set(SECKILL_ORDER + userId + goodsId, seckillOrder, 3600L);
            }
        } else {
            seckillOrder = JSONObject.parseObject(o.toString(), MySeckillOrder.class);
        }
        return seckillOrder;
    }

    /**
     * 分页查询list
     *
     * @param mySeckillOrder 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MySeckillOrder> queryList(MySeckillOrder mySeckillOrder) {
        return mySeckillOrderDao.queryList(mySeckillOrder);
    }

    /**
     * 新增数据
     *
     * @param mySeckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MySeckillOrder mySeckillOrder) {
        return mySeckillOrderDao.insert(mySeckillOrder);
    }

    /**
     * 修改数据
     *
     * @param mySeckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MySeckillOrder mySeckillOrder) {
        int update = mySeckillOrderDao.update(mySeckillOrder);
        if (update > 0) {
            refreshRedis(mySeckillOrder.getUserId(), mySeckillOrder.getGoodsId());
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
        return mySeckillOrderDao.deleteById(id) > 0;
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
