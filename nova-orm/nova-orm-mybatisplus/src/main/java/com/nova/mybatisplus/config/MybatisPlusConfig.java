package com.nova.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: wzh
 * @description mybatisPlus插件
 * @date: 2023/06/20 17:45
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @see <a href="https://baomidou.com/pages/2976a3/#mybatisplusinterceptor</a>
     * <p>
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //MybatisPlusInterceptor插件，默认提供分页插件，如需其他MP内置插件，则需自定义该Bean
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        //配置防止全表更新拦截器
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }


}
