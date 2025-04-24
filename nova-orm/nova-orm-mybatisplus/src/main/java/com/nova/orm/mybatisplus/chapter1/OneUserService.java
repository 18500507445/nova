package com.nova.orm.mybatisplus.chapter1;

import com.nova.orm.mybatisplus.entity.UserDO;

import java.util.List;

/**
 * @author: wzh
 * @description: userService
 * @date: 2023/06/15 19:52
 */
public interface OneUserService {

    List<UserDO> selectList();
}
