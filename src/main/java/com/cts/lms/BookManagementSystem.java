package com.cts.lms;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.cts.lms")
@EnableJpaRepositories("com.cts.lms.repository")
public class BookManagementSystem {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(BookManagementSystem.class, args);
    }
}
