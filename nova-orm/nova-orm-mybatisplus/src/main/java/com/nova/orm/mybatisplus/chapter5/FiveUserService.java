package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nova.orm.mybatisplus.entity.UserFiveDO;

/**
 * @author: wzh
 * @description userService
 * @date: 2023/06/15 19:52
 */
public interface FiveUserService extends IService<UserFiveDO> {

    /**
     * 测试 同数据源 事务
     */
    void theSame();

    /**
     * 测试 不同数据源 事务
     */
    void notAlike();
}