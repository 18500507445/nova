package com.nova.shopping.order.repository.mapper;

import com.nova.shopping.order.repository.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: wzh
 * @description: 订单表(MyOrder)表数据库访问层
 * @date: 2023-04-15 15:53
 */
@Mapper
public interface OrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Order queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param order 查询条件
     * @return 对象列表
     */
    List<Order> queryList(Order order);

    /**
     * 统计总行数
     *
     * @param order 查询条件
     * @return 总行数
     */
    long count(Order order);

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyOrder> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Order> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyOrder> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Order> entities);

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int update(Order order);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

