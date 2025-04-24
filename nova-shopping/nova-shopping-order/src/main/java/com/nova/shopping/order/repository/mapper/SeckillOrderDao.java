package com.nova.shopping.order.repository.mapper;

import com.nova.shopping.order.repository.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:wzh
 * @description: 秒杀订单表(MySeckillOrder)表数据库访问层
 * @date:2023-04-15 15:53
 */
@Mapper
public interface SeckillOrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param userId  用户id
     * @param goodsId 商品id
     * @return 实例对象
     */
    SeckillOrder queryById(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    /**
     * 查询指定行数据
     *
     * @param seckillOrder 查询条件
     * @return 对象列表
     */
    List<SeckillOrder> queryList(SeckillOrder seckillOrder);

    /**
     * 统计总行数
     *
     * @param seckillOrder 查询条件
     * @return 总行数
     */
    long count(SeckillOrder seckillOrder);

    /**
     * 新增数据
     *
     * @param seckillOrder 实例对象
     * @return 影响行数
     */
    int insert(SeckillOrder seckillOrder);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MySeckillOrder> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SeckillOrder> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MySeckillOrder> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SeckillOrder> entities);

    /**
     * 修改数据
     *
     * @param seckillOrder 实例对象
     * @return 影响行数
     */
    int update(SeckillOrder seckillOrder);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

