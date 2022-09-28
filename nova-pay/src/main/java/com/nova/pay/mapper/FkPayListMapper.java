package com.nova.pay.mapper;

import com.nova.pay.entity.result.FkPayList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/8/22 13:30
 */
@Mapper
public interface FkPayListMapper {

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
     * 删除支付列
     *
     * @param id 支付列主键
     * @return 结果
     */
    int deleteFkPayListById(Long id);

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFkPayListByIds(String[] ids);
}
