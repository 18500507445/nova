package com.nova.shopping.order.dao;

import com.nova.shopping.order.entity.MyGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wzh
 * @description: 商品表(MyGoods)表数据库访问层
 * @date 2023-04-15 15:53
 */
@Mapper
public interface MyGoodsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MyGoods queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param myGoods 查询条件
     * @return 对象列表
     */
    List<MyGoods> queryList(MyGoods myGoods);

    /**
     * 统计总行数
     *
     * @param myGoods 查询条件
     * @return 总行数
     */
    long count(MyGoods myGoods);

    /**
     * 新增数据
     *
     * @param myGoods 实例对象
     * @return 影响行数
     */
    int insert(MyGoods myGoods);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyGoods> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MyGoods> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyGoods> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MyGoods> entities);

    /**
     * 修改数据
     *
     * @param myGoods 实例对象
     * @return 影响行数
     */
    int update(MyGoods myGoods);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

