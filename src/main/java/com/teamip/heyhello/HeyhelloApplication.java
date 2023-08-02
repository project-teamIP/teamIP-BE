package com.teamip.heyhello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HeyhelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeyhelloApplication.class, args);
    }

}
