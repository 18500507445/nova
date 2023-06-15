package com.nova.mybatisplus.chapter1;

import com.nova.mybatisplus.entity.UserDO;

import java.util.List;

/**
 * @author: wzh
 * @description userService
 * @date: 2023/06/15 19:52
 */
public interface UserService {

    List<UserDO> selectList();
}
