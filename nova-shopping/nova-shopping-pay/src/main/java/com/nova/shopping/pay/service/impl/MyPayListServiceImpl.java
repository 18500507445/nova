package com.nova.shopping.pay.service.impl;

import cn.hutool.core.convert.Convert;
import com.nova.shopping.pay.dao.MyPayListDao;
import com.nova.shopping.pay.entity.MyPayList;
import com.nova.shopping.pay.service.MyPayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author wzh
 * @description: 支付列表(MyPayList)表服务实现类
 * @date 2023-04-14 19:27
 */
@Service("myPayListService")
@RequiredArgsConstructor
public class MyPayListServiceImpl implements MyPayListService {

    private final MyPayListDao myPayListDao;

    /**
     * 查询支付列
     *
     * @param id 支付列主键
     * @return 支付列
     */
    @Override
    public MyPayList selectMyPayListById(Long id) {
        return myPayListDao.selectMyPayListById(id);
    }

    /**
     * 查询支付列列表
     *
     * @param myPayList 支付列
     * @return 支付列
     */
    @Override
    public List<MyPayList> selectMyPayListList(MyPayList myPayList) {
        return myPayListDao.selectMyPayListList(myPayList);
    }

    /**
     * 新增支付列
     *
     * @param myPayList 支付列
     * @return 结果
     */
    @Override
    public int insertMyPayList(MyPayList myPayList) {
        return myPayListDao.insertMyPayList(myPayList);
    }

    /**
     * 修改支付列
     *
     * @param myPayList 支付列
     * @return 结果
     */
    @Override
    public int updateMyPayList(MyPayList myPayList) {
        return myPayListDao.updateMyPayList(myPayList);
    }

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的支付列主键
     * @return 结果
     */
    @Override
    public int deleteMyPayListByIds(String ids) {
        return myPayListDao.deleteMyPayListByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付列信息
     *
     * @param id 支付列主键
     * @return 结果
     */
    @Override
    public int deleteMyPayListById(Long id) {
        return myPayListDao.deleteMyPayListById(id);
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<MyPayList> list) {
        return myPayListDao.batchInsert(list);
    }
}
