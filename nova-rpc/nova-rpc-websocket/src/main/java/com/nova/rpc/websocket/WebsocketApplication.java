package com.nova.rpc.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wzh
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
        System.err.println("访问 ===> http://127.0.0.1:8080/");
    }

}
