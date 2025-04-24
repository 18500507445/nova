package com.nova.shopping.pay.service.pay.impl;

import cn.hutool.core.convert.Convert;
import com.nova.shopping.pay.repository.mapper.PayListDao;
import com.nova.shopping.pay.repository.entity.PayList;
import com.nova.shopping.pay.service.pay.PayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author: wzh
 * @description: 支付列表(MyPayList)表服务实现类
 * @date: 2023-04-14 19:27
 */
@Service("myPayListService")
@RequiredArgsConstructor
public class PayListServiceImpl implements PayListService {

    private final PayListDao payListDao;

    /**
     * 查询支付列
     *
     * @param id 支付列主键
     * @return 支付列
     */
    @Override
    public PayList selectMyPayListById(Long id) {
        return payListDao.selectMyPayListById(id);
    }

    /**
     * 查询支付列列表
     *
     * @param payList 支付列
     * @return 支付列
     */
    @Override
    public List<PayList> selectMyPayListList(PayList payList) {
        return payListDao.selectMyPayListList(payList);
    }

    /**
     * 新增支付列
     *
     * @param payList 支付列
     * @return 结果
     */
    @Override
    public int insertMyPayList(PayList payList) {
        return payListDao.insertMyPayList(payList);
    }

    /**
     * 修改支付列
     *
     * @param payList 支付列
     * @return 结果
     */
    @Override
    public int updateMyPayList(PayList payList) {
        return payListDao.updateMyPayList(payList);
    }

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的支付列主键
     * @return 结果
     */
    @Override
    public int deleteMyPayListByIds(String ids) {
        return payListDao.deleteMyPayListByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付列信息
     *
     * @param id 支付列主键
     * @return 结果
     */
    @Override
    public int deleteMyPayListById(Long id) {
        return payListDao.deleteMyPayListById(id);
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<PayList> list) {
        return payListDao.batchInsert(list);
    }
}
