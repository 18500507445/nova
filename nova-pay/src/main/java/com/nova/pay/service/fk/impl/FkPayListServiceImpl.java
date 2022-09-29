package com.nova.pay.service.fk.impl;

import cn.hutool.core.convert.Convert;
import com.nova.pay.entity.result.FkPayList;
import com.nova.pay.mapper.FkPayListMapper;
import com.nova.pay.service.fk.FkPayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description: 支付列表Service业务层处理
 * @Author: wangzehui
 * @Date: 2022/8/22 13:29
 */
@Service
public class FkPayListServiceImpl implements FkPayListService {

    @Autowired
    private FkPayListMapper fkPayListMapper;

    /**
     * 查询支付列
     *
     * @param id 支付列主键
     * @return 支付列
     */
    @Override
    public FkPayList selectFkPayListById(Long id) {
        return fkPayListMapper.selectFkPayListById(id);
    }

    /**
     * 查询支付列列表
     *
     * @param fkPayList 支付列
     * @return 支付列
     */
    @Override
    public List<FkPayList> selectFkPayListList(FkPayList fkPayList) {
        return fkPayListMapper.selectFkPayListList(fkPayList);
    }

    /**
     * 新增支付列
     *
     * @param fkPayList 支付列
     * @return 结果
     */
    @Override
    public int insertFkPayList(FkPayList fkPayList) {
        return fkPayListMapper.insertFkPayList(fkPayList);
    }

    /**
     * 修改支付列
     *
     * @param fkPayList 支付列
     * @return 结果
     */
    @Override
    public int updateFkPayList(FkPayList fkPayList) {
        return fkPayListMapper.updateFkPayList(fkPayList);
    }

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的支付列主键
     * @return 结果
     */
    @Override
    public int deleteFkPayListByIds(String ids) {
        return fkPayListMapper.deleteFkPayListByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付列信息
     *
     * @param id 支付列主键
     * @return 结果
     */
    @Override
    public int deleteFkPayListById(Long id) {
        return fkPayListMapper.deleteFkPayListById(id);
    }

    /**
     * 批量插入
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<FkPayList> list) {
        return fkPayListMapper.batchInsert(list);
    }
}
