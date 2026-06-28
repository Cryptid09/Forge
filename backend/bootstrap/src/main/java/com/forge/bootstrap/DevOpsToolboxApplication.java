package com.forge.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.forge"})
@EnableJpaRepositories(basePackages = {"com.forge.infrastructure.persistence.repository"})
@EntityScan(basePackages = {"com.forge.infrastructure.persistence.entity"})
public class DevOpsToolboxApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevOpsToolboxApplication.class, args);
    }
}
