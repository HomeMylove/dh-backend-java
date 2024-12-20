package com.homemylove;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.homemylove.mapper")
public class DHApplication {

    public static void main(String[] args) {
        SpringApplication.run(DHApplication.class, args);
    }

}
