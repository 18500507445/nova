package com.nova.log.logback.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;

/**
 * @author: wzh
 * @description: 日志实体类
 * @date: 2023/08/01 16:16
 */
@Component
@RequestScope
@Data
public class LogEntity {

    private String path;

    private Map<String, String[]> params;

    private Object req;

    private Object resp;
}
