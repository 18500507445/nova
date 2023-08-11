package com.nova.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
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
public class AliEasyExportDO extends BaseDO {

    @ExcelProperty(value = "id", index = 0)
    private Long id;

    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "年龄", index = 2)
    private Integer age;

    @ExcelProperty(value = "订单id", index = 3)
    private String orderId;

    @ExcelProperty(value = "状态", index = 4)
    private Integer status;

    @ExcelProperty(value = "创建时间", index = 5)
    private String createTime;

    @ExcelProperty(value = "修改时间", index = 6)
    private String updateTime;
}
