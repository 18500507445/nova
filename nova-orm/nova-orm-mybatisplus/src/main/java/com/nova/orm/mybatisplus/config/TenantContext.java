package com.nova.orm.mybatisplus.config;

import com.nova.orm.mybatisplus.entity.Tenant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.NamedThreadLocal;

/**
 * @author: wzh
 * @description: 租户上下文
 * @date: 2025/08/15 10:32
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TenantContext {

    /**
     * Tenant对象上下文
     */
    public static final ThreadLocal<Tenant> TENANT_CONTEXT = new NamedThreadLocal<>("Tenant Context");

    /**
     * 设置租户
     */
    public static Tenant setTenant(Long tenantId) {
        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        TENANT_CONTEXT.set(tenant);
        return tenant;
    }

    /**
     * 获取租户
     */
    public static Tenant getTenant() {
        return TENANT_CONTEXT.get();
    }

    /**
     * 清空Tenant对象
     */
    public static void remove() {
        TENANT_CONTEXT.remove();
    }

}
