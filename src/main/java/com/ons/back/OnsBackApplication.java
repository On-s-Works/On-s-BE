package com.ons.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OnsBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnsBackApplication.class, args);
    }

}
