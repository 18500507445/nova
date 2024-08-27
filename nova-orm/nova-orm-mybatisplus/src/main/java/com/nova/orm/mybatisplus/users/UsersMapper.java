package com.nova.orm.mybatisplus.users;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("study")
public interface UsersMapper extends BaseMapper<Users> {

}