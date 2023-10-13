package com.nova.shopping.order.dao;

import com.nova.shopping.order.entity.MyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wzh
 * @description: 订单表(MyOrder)表数据库访问层
 * @date 2023-04-15 15:53
 */
@Mapper
public interface MyOrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MyOrder queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param myOrder 查询条件
     * @return 对象列表
     */
    List<MyOrder> queryList(MyOrder myOrder);

    /**
     * 统计总行数
     *
     * @param myOrder 查询条件
     * @return 总行数
     */
    long count(MyOrder myOrder);

    /**
     * 新增数据
     *
     * @param myOrder 实例对象
     * @return 影响行数
     */
    int insert(MyOrder myOrder);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyOrder> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MyOrder> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyOrder> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MyOrder> entities);

    /**
     * 修改数据
     *
     * @param myOrder 实例对象
     * @return 影响行数
     */
    int update(MyOrder myOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

