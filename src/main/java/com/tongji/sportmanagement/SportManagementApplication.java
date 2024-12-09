package com.tongji.sportmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.tongji.sportmanagement.Mapper")
@SpringBootApplication
public class SportManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportManagementApplication.class, args);
    }

}
