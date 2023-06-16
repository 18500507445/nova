package com.nova.mybatisplus.chapter1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.mybatisplus.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description userMapper
 * @date: 2023/06/15 19:50
 */
@Mapper
public interface OneUserMapper extends BaseMapper<UserDO> {

}
