package com.nova.orm.mybatisplus.chapter6;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: wzh
 * @description: 动态user表，TableName不需要了
 * @date: 2025/08/15 10:10
 */
@Data
@Accessors(chain = true)
public class DynamicUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //租户 ID
    @TableField("tenant_id")
    private Long tenantId;

    @TableField("name")
    private String name;
}
