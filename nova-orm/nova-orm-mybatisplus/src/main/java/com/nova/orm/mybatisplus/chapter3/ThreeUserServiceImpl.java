package com.nova.orm.mybatisplus.chapter3;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
public class ThreeUserServiceImpl extends ServiceImpl<ThreeUserMapper, UserDO> implements ThreeUserService {

}
