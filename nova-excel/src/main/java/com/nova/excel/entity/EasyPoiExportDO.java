package com.nova.excel.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.nova.common.core.model.pojo.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @description: 模拟数据库实体类
 * @author: wzh
 * @date: 2023/1/12 13:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EasyPoiExportDO extends BaseDO {

    @Excel(name = "id", isImportField = "true", orderNum = "1")
    private Long id;

    @Excel(name = "姓名", orderNum = "1")
    private String name;

    @Excel(name = "年龄", orderNum = "2")
    private Integer age;

    @Excel(name = "订单id", orderNum = "3")
    private String orderId;

    @Excel(name = "状态", orderNum = "4")
    private Integer status;

    @Excel(name = "创建时间", orderNum = "5")
    private String createTime;

    @Excel(name = "修改时间", orderNum = "6")
    private String updateTime;
}
