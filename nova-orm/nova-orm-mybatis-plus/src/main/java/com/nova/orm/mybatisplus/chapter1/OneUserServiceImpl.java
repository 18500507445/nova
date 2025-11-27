package com.nova.orm.mybatisplus.chapter1;

import com.nova.orm.mybatisplus.entity.UserDO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: wzh
 * @description: userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
@AllArgsConstructor
public class OneUserServiceImpl implements OneUserService {

    private final OneUserMapper oneUserMapper;

    @Override
    public List<UserDO> selectList() {
        return oneUserMapper.selectList(null);
    }
}
