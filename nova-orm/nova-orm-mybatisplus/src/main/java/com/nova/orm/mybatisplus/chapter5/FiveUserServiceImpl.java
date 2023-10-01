package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.orm.mybatisplus.entity.UserFiveDO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description userServiceImpl
 * @date: 2023/06/15 19:54
 */
@Service
@AllArgsConstructor
public class FiveUserServiceImpl extends ServiceImpl<FiveUserMapper, UserFiveDO> implements FiveUserService {

    private final FiveUserMapper fiveUserMapper;

    private final MyOrderMapper myOrderMapper;

    @Override
    @DSTransactional
    public void theSame() {
        UserFiveDO one = new UserFiveDO();
        one.setName("sameOne").setAge(1).setEmail("sameOne@qq.com").setGender(GenderEnum.MAN);
        fiveUserMapper.insert(one);

        UserFiveDO two = new UserFiveDO();
        two.setName("sameTwo").setAge(1).setEmail("sameTwo@163.com").setGender(GenderEnum.WOMAN);
        fiveUserMapper.insert(two);

        //模拟异常
        int i = 1 / 0;
    }

    @Override
    @DSTransactional
    public void notAlike() {
        UserFiveDO one = new UserFiveDO();
        one.setName("notAlike").setAge(1).setEmail("notAlike@qq.com").setGender(GenderEnum.MAN);
        fiveUserMapper.insert(one);

        myOrderMapper.deleteById(2L);

        //模拟异常
        int i = 1 / 0;
    }
}
