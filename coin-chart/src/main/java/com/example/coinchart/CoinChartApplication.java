package com.example.coinchart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class CoinChartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinChartApplication.class, args);
    }
    
}
