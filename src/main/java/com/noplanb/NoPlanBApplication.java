package com.noplanb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoPlanBApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoPlanBApplication.class, args);
    }

}
