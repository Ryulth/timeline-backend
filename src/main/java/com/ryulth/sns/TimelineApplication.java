package com.ryulth.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TimelineApplication {
    public static void main(String args[]) {
        SpringApplication.run(TimelineApplication.class, args);
    }
}
