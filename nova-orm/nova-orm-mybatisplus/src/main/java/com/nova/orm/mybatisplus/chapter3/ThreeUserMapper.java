package com.nova.orm.mybatisplus.chapter3;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description userMapper
 * @date: 2023/06/15 19:50
 */
@Mapper
public interface ThreeUserMapper extends BaseMapper<UserDO> {

}