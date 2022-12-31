package com.nova.pay.service.fk;


import com.nova.pay.entity.result.FkPayList;

import java.util.List;

/**
 * @description: 支付列表
 * @author: wzh
 * @date: 2022/8/22 13:21
 */
public interface FkPayListService {

    /**
     * 查询支付列
     *
     * @param id 支付列主键
     * @return 支付列
     */
    FkPayList selectFkPayListById(Long id);

    /**
     * 查询支付列列表
     *
     * @param fkPayList 支付列
     * @return 支付列集合
     */
    List<FkPayList> selectFkPayListList(FkPayList fkPayList);

    /**
     * 新增支付列
     *
     * @param fkPayList 支付列
     * @return 结果
     */
    int insertFkPayList(FkPayList fkPayList);

    /**
     * 修改支付列
     *
     * @param fkPayList 支付列
     * @return 结果
     */
    int updateFkPayList(FkPayList fkPayList);

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的支付列主键集合
     * @return 结果
     */
    int deleteFkPayListByIds(String ids);

    /**
     * 删除支付列信息
     *
     * @param id 支付列主键
     * @return 结果
     */
    int deleteFkPayListById(Long id);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(List<FkPayList> list);
}
