package com.nova.pay.mapper;

import com.nova.pay.entity.result.FkPayConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @description:
 * @author: wangzehui
 * @date: 2022/8/22 15:40
 */
@Mapper
public interface FkPayConfigMapper {

    /**
     * 查询支付配置
     *
     * @param id 支付配置主键
     * @return 支付配置
     */
    FkPayConfig selectFkPayConfigById(Long id);

    /**
     * 查询支付配置列表
     *
     * @param fkPayConfig 支付配置
     * @return 支付配置集合
     */
    List<FkPayConfig> selectFkPayConfigList(FkPayConfig fkPayConfig);

    /**
     * 新增支付配置
     *
     * @param fkPayConfig 支付配置
     * @return 结果
     */
    int insertFkPayConfig(FkPayConfig fkPayConfig);

    /**
     * 修改支付配置
     *
     * @param fkPayConfig 支付配置
     * @return 结果
     */
    int updateFkPayConfig(FkPayConfig fkPayConfig);

    /**
     * 删除支付配置
     *
     * @param id 支付配置主键
     * @return 结果
     */
    int deleteFkPayConfigById(Long id);

    /**
     * 批量删除支付配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFkPayConfigByIds(String[] ids);
}
