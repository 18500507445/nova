package com.nova.database.annotation;


import com.nova.database.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @description: 自定义多数据源切换注解
 * @author: wzh
 * @date: 2022/8/4 21:29
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
