package com.nova.orm.mybatisplus.chapter6;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: wzh
 * @description: 租户
 * @date: 2025/08/15 09:52
 */
@Data
@Accessors(chain = true)
@TableName("tenant_user")
public class TenantUser {

    private Long id;

    /**
     * 租户 ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    @TableField("name")
    private String name;

}
