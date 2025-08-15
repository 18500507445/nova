package com.nova.orm.mybatisplus.chapter6;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description: 动态userMapper
 * @date: 2025/08/15 10:12
 */
@Mapper
@DS("study")
public interface DynamicUserMapper extends BaseMapper<DynamicUser> {

}
