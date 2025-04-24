package com.nova.shopping.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.order.repository.mapper.GoodsDao;
import com.nova.shopping.order.repository.entity.Goods;
import com.nova.shopping.order.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wzh
 * @description: 商品表(MyGoods)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("myGoodsService")
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final RedisService redisService;

    private final GoodsDao goodsDao;

    private static final String TOTAL_COUNT_KEY = Constants.REDIS_KEY + Goods.TOTAL_COUNT;

    private static final String GOODS = Constants.REDIS_KEY + Goods.GOODS;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Goods queryById(Long id) {
        Object o = redisService.get(GOODS + id);
        Goods goods;
        if (ObjectUtil.isNull(o)) {
            goods = goodsDao.queryById(id);
            if (ObjectUtil.isNotNull(goods)) {
                redisService.set(GOODS + id, JSONObject.toJSONString(goods), 3600L);
                redisService.set(TOTAL_COUNT_KEY + id, goods.getTotal(), 3600L);
            }
        } else {
            goods = JSONObject.parseObject(o.toString(), Goods.class);
        }
        return goods;
    }

    /**
     * 分页查询list
     *
     * @param goods 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Goods> queryList(Goods goods) {
        return goodsDao.queryList(goods);
    }

    /**
     * 新增数据
     *
     * @param goods 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Goods goods) {
        return goodsDao.insert(goods);
    }

    /**
     * 修改数据，并刷新缓存
     *
     * @param goods 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Goods goods) {
        int update = goodsDao.update(goods);
        if (update > 0) {
            refreshRedis(goods.getId());
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
        return goodsDao.deleteById(id) > 0;
    }

    /**
     * 刷新缓存
     * 删除商品缓存和售卖总数缓存
     *
     * @param id
     */
    public void refreshRedis(Long id) {
        redisService.del(GOODS + id, TOTAL_COUNT_KEY + id);
    }
}
