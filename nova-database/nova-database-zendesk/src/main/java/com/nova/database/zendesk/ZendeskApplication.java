package com.nova.database.zendesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @description: Zendesk启动类，监听binlog
 * @author: wzh
 * @date: 2024/07/30 13:57
 * @see <a href="https://blog.csdn.net/m0_37583655/article/details/119148470">Java监听mysql的binlog</a>
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ZendeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZendeskApplication.class, args);
    }

}
