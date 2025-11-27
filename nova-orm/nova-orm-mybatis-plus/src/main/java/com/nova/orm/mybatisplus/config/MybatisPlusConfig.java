package com.nova.orm.mybatisplus.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.nova.orm.mybatisplus.entity.Tenant;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: wzh
 * @description: mybatisPlus插件
 * @date: 2023/06/20 17:45
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @see <a href="https://baomidou.com/pages/2976a3/#mybatisplusinterceptor">插件官方说明</a>
     * <p>
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //MybatisPlusInterceptor插件，默认提供分页插件，如需其他MP内置插件，则需自定义该Bean
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //分页插件 用依赖包自己的Page对象，不是pagehelper的Page
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        //配置防止全表更新拦截器
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        //多租户配置插件
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {

            //租户默认值1
            @Override
            public Expression getTenantId() {
                return new LongValue(1);
            }

            //租户数据库默认字段值
            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            //只有tenant_user表生效，sql自带tenant_id条件
            @Override
            public boolean ignoreTable(String tableName) {
                return !"tenant_user".equalsIgnoreCase(tableName);
            }
        }));

        //动态表名插件
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = getDynamicTableNameInnerInterceptor();
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        return interceptor;
    }

    private static DynamicTableNameInnerInterceptor getDynamicTableNameInnerInterceptor() {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            //仅dynamic_user进行处理
            if (StrUtil.contains(tableName, "dynamic_user")) {
                Tenant tenant = TenantContext.getTenant();
                if (ObjectUtil.isNotNull(tenant) && ObjectUtil.isNotNull(tenant.getTenantId())) {
                    tableName = tableName + "_" + tenant.getTenantId();
                }
            }
            return tableName;
        });
        return dynamicTableNameInnerInterceptor;
    }


}
