package com.nova.orm.mybatisflex.mapper;

import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.BaseMapper;
import com.nova.orm.mybatisflex.entity.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description
 * @date: 2023/07/30 14:08
 */
@Mapper
@UseDataSource("master")
public interface AccountMapper extends BaseMapper<Account> {

}