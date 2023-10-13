package com.nova.shopping.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.config.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.order.dao.MyGoodsDao;
import com.nova.shopping.order.entity.MyGoods;
import com.nova.shopping.order.service.MyGoodsService;
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
public class MyGoodsServiceImpl implements MyGoodsService {

    private final RedisService redisService;

    private final MyGoodsDao myGoodsDao;

    private static final String TOTAL_COUNT_KEY = Constants.REDIS_KEY + MyGoods.TOTAL_COUNT;

    private static final String GOODS = Constants.REDIS_KEY + MyGoods.GOODS;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MyGoods queryById(Long id) {
        Object o = redisService.get(GOODS + id);
        MyGoods myGoods;
        if (ObjectUtil.isNull(o)) {
            myGoods = myGoodsDao.queryById(id);
            if (ObjectUtil.isNotNull(myGoods)) {
                redisService.set(GOODS + id, JSONObject.toJSONString(myGoods), 3600L);
                redisService.set(TOTAL_COUNT_KEY + id, myGoods.getTotal(), 3600L);
            }
        } else {
            myGoods = JSONObject.parseObject(o.toString(), MyGoods.class);
        }
        return myGoods;
    }

    /**
     * 分页查询list
     *
     * @param myGoods 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MyGoods> queryList(MyGoods myGoods) {
        return myGoodsDao.queryList(myGoods);
    }

    /**
     * 新增数据
     *
     * @param myGoods 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MyGoods myGoods) {
        return myGoodsDao.insert(myGoods);
    }

    /**
     * 修改数据，并刷新缓存
     *
     * @param myGoods 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MyGoods myGoods) {
        int update = myGoodsDao.update(myGoods);
        if (update > 0) {
            refreshRedis(myGoods.getId());
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
        return myGoodsDao.deleteById(id) > 0;
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
