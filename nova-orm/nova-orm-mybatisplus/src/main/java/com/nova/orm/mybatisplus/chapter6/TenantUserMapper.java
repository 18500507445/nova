package com.nova.orm.mybatisplus.chapter6;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description: 租户mapper
 * @date: 2025/08/15 09:54
 */
@Mapper
@DS("study")
public interface TenantUserMapper extends BaseMapper<TenantUser> {

}
