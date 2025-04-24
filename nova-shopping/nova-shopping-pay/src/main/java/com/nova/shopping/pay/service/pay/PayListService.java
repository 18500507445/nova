package com.nova.shopping.pay.service.pay;


import com.nova.shopping.pay.repository.entity.PayList;

import java.util.List;

/**
 * @author:wzh
 * @description: 支付列表(MyPayList)表服务接口
 * @date:2023-04-14 19:27
 */
public interface PayListService {

    /**
     * 查询支付列
     *
     * @param id 支付列主键
     * @return 支付列
     */
    PayList selectMyPayListById(Long id);

    /**
     * 查询支付列列表
     *
     * @param payList 支付列
     * @return 支付列集合
     */
    List<PayList> selectMyPayListList(PayList payList);

    /**
     * 新增支付列
     *
     * @param payList 支付列
     * @return 结果
     */
    int insertMyPayList(PayList payList);

    /**
     * 修改支付列
     *
     * @param payList 支付列
     * @return 结果
     */
    int updateMyPayList(PayList payList);

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的支付列主键集合
     * @return 结果
     */
    int deleteMyPayListByIds(String ids);

    /**
     * 删除支付列信息
     *
     * @param id 支付列主键
     * @return 结果
     */
    int deleteMyPayListById(Long id);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int batchInsert(List<PayList> list);

}
