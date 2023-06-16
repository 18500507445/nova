package com.nova.mybatisplus.chapter2;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.mybatisplus.entity.UserDO;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
public class TwoUserServiceImpl extends ServiceImpl<TwoUserMapper, UserDO> implements TwoUserService {

}
