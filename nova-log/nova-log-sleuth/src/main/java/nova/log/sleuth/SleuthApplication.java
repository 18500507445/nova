package nova.log.sleuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: wzh
 * @description cloud-sleuth链路追踪
 * @date: 2023/10/12 15:43
 */
@SpringBootApplication
public class SleuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class, args);
    }
}