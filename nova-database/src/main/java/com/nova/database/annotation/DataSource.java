package com.nova.database.annotation;


import com.nova.common.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @Description: 自定义多数据源切换注解
 * @Author: wangzehui
 * @Date: 2022/8/4 21:29
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
