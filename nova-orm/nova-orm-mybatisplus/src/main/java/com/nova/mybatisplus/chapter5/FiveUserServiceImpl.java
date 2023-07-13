package com.nova.mybatisplus.chapter5;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.mybatisplus.entity.UserFiveDO;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
public class FiveUserServiceImpl extends ServiceImpl<FiveUserMapper, UserFiveDO> implements FiveUserService {

}
