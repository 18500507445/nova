package com.nova.rpc.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @description: 启动 boot 对 websocket 的支持
 * @author: wzh
 * @date: 2022/1/7 20:13
 */
@Configuration
public class WebSocketConfiguration {

    /**
     * 给 spring 容器注入这个 ServerEndpointExporter对象
     * <p>
     * 这个bean会检测所有带有 @ServerEndpoint 注解的 bean 并注册他们。
     * tips：如果使用的是外置的 Tomcat 容器，则不需要自己提供 ServerEndpointExporter，因为它将由 Tomcat 容器自己提供和管理。
     *
     * @return ServerEndpointExporter
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
