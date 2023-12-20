package com.nova.orm.mybatisplus.chapter4;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nova.orm.mybatisplus.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: wzh
 * @description userMapper
 * @date: 2023/06/15 19:50
 */
@Mapper
@DS("study")
public interface FourUserMapper extends BaseMapper<UserDO> {

    IPage<UserDO> selectByName(@Param("page") IPage<UserDO> page, @Param("name") String name);

}
