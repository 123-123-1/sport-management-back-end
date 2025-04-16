package com.tongji.sportmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SportManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportManagementApplication.class, args);
    }

}
