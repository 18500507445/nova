package com.nova.shopping.pay.service.pay;


import com.nova.shopping.pay.entity.MyPayConfig;

import java.util.List;

/**
 * @author wzh
 * @description: 支付配置表(MyPayConfig)表服务接口
 * @date 2023-04-14 19:27
 */
public interface MyPayConfigService {

    /**
     * 查询支付配置
     *
     * @param id 支付配置主键
     * @return 支付配置
     */
    MyPayConfig selectMyPayConfigById(Long id);

    /**
     * 查询支付配置列表
     *
     * @param myPayConfig 支付配置
     * @return 支付配置集合
     */
    List<MyPayConfig> selectMyPayConfigList(MyPayConfig myPayConfig);

    /**
     * 新增支付配置
     *
     * @param myPayConfig 支付配置
     * @return 结果
     */
    int insertMyPayConfig(MyPayConfig myPayConfig);

    /**
     * 修改支付配置
     *
     * @param myPayConfig 支付配置
     * @return 结果
     */
    int updateMyPayConfig(MyPayConfig myPayConfig);

    /**
     * 批量删除支付配置
     *
     * @param ids 需要删除的支付配置主键集合
     * @return 结果
     */
    int deleteMyPayConfigByIds(String ids);

    /**
     * 删除支付配置信息
     *
     * @param id 支付配置主键
     * @return 结果
     */
    int deleteMyPayConfigById(Long id);

    /**
     * 随机查询一个配置
     *
     * @param source
     * @param sid
     * @param payWay
     * @return
     */
    MyPayConfig getRandomConfigData(String source, String sid, String payWay);

    /**
     * 根据红单支付类型查询配置
     *
     * @param payConfigId
     * @return
     */
    MyPayConfig getConfigData(Long payConfigId);

}
