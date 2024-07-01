package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.orm.mybatisplus.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description orderMapper
 * @date: 2023/06/21 17:04
 */
@Mapper
@DS("shopping")
public interface OrderMapper extends BaseMapper<Order> {

}
