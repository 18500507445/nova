package com.nova.pay.service.fk;


import com.nova.pay.entity.result.FkPayConfig;

import java.util.List;

/**
 * @Description: 支付配置Service接口
 * @Author: wangzehui
 * @Date: 2022/8/22 15:43
 */
public interface FkPayConfigService {

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
     * 批量删除支付配置
     *
     * @param ids 需要删除的支付配置主键集合
     * @return 结果
     */
    int deleteFkPayConfigByIds(String ids);

    /**
     * 删除支付配置信息
     *
     * @param id 支付配置主键
     * @return 结果
     */
    int deleteFkPayConfigById(Long id);

    /**
     * 随机查询一个配置
     *
     * @param source
     * @param sid
     * @param payWay
     * @return
     */
    FkPayConfig getRandomConfigData(String source, String sid, String payWay);

    /**
     * 根据红单支付类型查询配置
     *
     * @param payConfigId
     * @return
     */
    FkPayConfig getConfigData(Long payConfigId);
}
