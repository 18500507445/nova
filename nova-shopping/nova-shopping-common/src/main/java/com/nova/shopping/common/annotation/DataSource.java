package com.nova.shopping.common.annotation;


import com.nova.shopping.common.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @description: 多数据源切换注解
 * @author: wzh
 * @date: 2023/4/14 18:15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    /**
     * 切换数据源名称
     */
    DataSourceType value() default DataSourceType.MASTER;
}
