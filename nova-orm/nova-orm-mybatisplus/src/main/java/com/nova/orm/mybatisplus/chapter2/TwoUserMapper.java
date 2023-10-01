package com.nova.orm.mybatisplus.chapter2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: wzh
 * @description userMapper
 * @date: 2023/06/15 19:50
 */
@Mapper
public interface TwoUserMapper extends BaseMapper<UserDO> {

    UserDO selectByName(@Param("name") String name);
}
