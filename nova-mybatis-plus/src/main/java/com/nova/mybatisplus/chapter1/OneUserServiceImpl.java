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
public class OneUserServiceImpl implements OneUserService {

    @Resource
    private OneUserMapper oneUserMapper;

    @Override
    public List<UserDO> selectList() {
        return oneUserMapper.selectList(null);
    }
}
