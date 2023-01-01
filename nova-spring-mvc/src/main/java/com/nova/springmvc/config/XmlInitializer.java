package com.nova.springmvc.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @description: 取代了web.xml的方式
 * @author: wzh
 * @date: 2023/1/1 11:30
 */
public class XmlInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        //基本的Spring配置类，一般用于业务层配置
        return new Class[]{MainConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        //配置DispatcherServlet的配置类、主要用于Controller等配置
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        //匹配路径，与上面一致
        return new String[]{"/"};
    }
}
