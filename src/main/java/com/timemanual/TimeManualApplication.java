package com.timemanual;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.timemanual.dao")
public class TimeManualApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeManualApplication.class, args);
    }
}
