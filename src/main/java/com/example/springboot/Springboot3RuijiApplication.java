package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching //开启spring-cache注解方式的缓存功能
public class Springboot3RuijiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot3RuijiApplication.class, args);
    }

}
