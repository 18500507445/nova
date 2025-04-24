package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nova.orm.mybatisplus.entity.UserFiveDO;

/**
 * @author: wzh
 * @description: userService
 * @date: 2023/06/15 19:52
 */
public interface FiveUserService extends IService<UserFiveDO> {

    void defaultCase();

    /**
     * 测试mp 同数据源 事务
     */
    void theSame();

    /**
     * 测试mp 不同数据源 事务
     */
    void notAlike();

    /**
     * 测试mp 手动管理事务
     */
    void manual();

}