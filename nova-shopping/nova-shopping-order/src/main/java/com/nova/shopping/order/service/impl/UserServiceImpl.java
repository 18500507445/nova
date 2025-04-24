package com.nova.shopping.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.nova.shopping.common.config.redis.RedisService;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.order.repository.mapper.UserDao;
import com.nova.shopping.order.repository.entity.User;
import com.nova.shopping.order.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wzh
 * @description: 用户表(MyUser)表服务实现类
 * @date: 2023-04-15 15:53
 */
@Service("myUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RedisService redisService;

    private final UserDao userDao;

    private static final String USER = Constants.REDIS_KEY + User.USER;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long id) {
        Object o = redisService.get(USER + id);
        User user;
        if (ObjectUtil.isNull(o)) {
            user = userDao.queryById(id);
            if (ObjectUtil.isNotNull(user)) {
                redisService.set(USER + id, JSONObject.toJSONString(user), 3600L);
            }
        } else {
            user = JSONObject.parseObject(o.toString(), User.class);
        }
        return user;
    }

    /**
     * 分页查询list
     *
     * @param user 筛选条件
     * @return 查询结果
     */
    @Override
    public List<User> queryList(User user) {
        return userDao.queryList(user);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(User user) {
        return userDao.insert(user);
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public int update(User user) {
        int update = userDao.update(user);
        if (update > 0) {
            refreshRedis(user.getId());
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
        return userDao.deleteById(id) > 0;
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
