package com.nova.tools.demo.springboot.aware;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author: wzh
 * @description EnvironmentAware，获取运行环境中变量
 * @date: 2023/10/08 17:48
 */
@Component
public class TestEnvironmentAware implements EnvironmentAware {

    @Override
    public void setEnvironment(@NotNull Environment environment) {
        System.err.println("[TestEnvironmentAware] 初始化" + Arrays.toString(environment.getActiveProfiles()));
    }
}
