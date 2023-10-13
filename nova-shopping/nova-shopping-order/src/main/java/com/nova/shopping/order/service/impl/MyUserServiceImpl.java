package com.nova.shopping.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.order.dao.MyUserDao;
import com.nova.shopping.order.entity.MyUser;
import com.nova.shopping.order.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wzh
 * @description: 用户表(MyUser)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("myUserService")
@RequiredArgsConstructor
public class MyUserServiceImpl implements MyUserService {

    private final RedisService redisService;

    private final MyUserDao myUserDao;

    private static final String USER = Constants.REDIS_KEY + MyUser.USER;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MyUser queryById(Long id) {
        Object o = redisService.get(USER + id);
        MyUser myUser;
        if (ObjectUtil.isNull(o)) {
            myUser = myUserDao.queryById(id);
            if (ObjectUtil.isNotNull(myUser)) {
                redisService.set(USER + id, JSONObject.toJSONString(myUser), 3600L);
            }
        } else {
            myUser = JSONObject.parseObject(o.toString(), MyUser.class);
        }
        return myUser;
    }

    /**
     * 分页查询list
     *
     * @param myUser 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MyUser> queryList(MyUser myUser) {
        return myUserDao.queryList(myUser);
    }

    /**
     * 新增数据
     *
     * @param myUser 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MyUser myUser) {
        return myUserDao.insert(myUser);
    }

    /**
     * 修改数据
     *
     * @param myUser 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MyUser myUser) {
        int update = myUserDao.update(myUser);
        if (update > 0) {
            refreshRedis(myUser.getId());
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
        return myUserDao.deleteById(id) > 0;
    }

    /**
     * 刷新缓存
     *
     * @param id
     */
    public void refreshRedis(Long id) {
        redisService.del(USER + id);
    }
}
