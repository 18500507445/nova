package com.nova.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.nova.common.core.model.pojo.BaseDO;
import com.nova.excel.utils.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

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

    /**
     * ⚠️：动态表头方法.includeColumnFieldNames()，使用动态表头就需要把index去掉，否则没有的字段的列会空出来，不会顺序写列数据
     */
    @ExcelProperty(value = "id", index = 0)
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "年龄", index = 2)
    private Integer age;

    @ExcelProperty(value = "订单id", index = 3)
    private String orderId;

    @ExcelProperty(value = "状态", index = 4)
    private Integer status;

    @ExcelProperty(value = "创建时间", index = 5, converter = DateConverter.class)
    private Date createTime;

    @ExcelProperty(value = "修改时间", index = 6)
    private String updateTime;

    //字段忽略
    @ExcelIgnore
    private String ignore;
}
