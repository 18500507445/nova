package com.nova.mybatisplus.chapter5;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.mybatisplus.entity.MyOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: wzh
 * @description orderMapper
 * @date: 2023/06/21 17:04
 */
@Mapper
@DS("my_mall")
public interface MyOrderMapper extends BaseMapper<MyOrder> {

}