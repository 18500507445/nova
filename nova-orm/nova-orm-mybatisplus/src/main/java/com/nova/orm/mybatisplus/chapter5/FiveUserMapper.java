package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.orm.mybatisplus.entity.UserFiveDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description userMapper
 * @date: 2023/06/15 19:50
 */
@Mapper
@DS("study")
public interface FiveUserMapper extends BaseMapper<UserFiveDO> {

}
