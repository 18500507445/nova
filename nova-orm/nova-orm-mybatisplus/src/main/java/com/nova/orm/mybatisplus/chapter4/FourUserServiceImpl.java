package com.nova.orm.mybatisplus.chapter4;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description: userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
public class FourUserServiceImpl extends ServiceImpl<FourUserMapper, UserDO> implements FourUserService {

}
