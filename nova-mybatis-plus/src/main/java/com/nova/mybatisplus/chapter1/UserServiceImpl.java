package com.nova.mybatisplus.chapter1;

import com.nova.mybatisplus.entity.UserDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wzh
 * @description userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserDO> selectList() {
        return userMapper.selectList(null);
    }
}
