package com.noplanb.domain;

import io.jsonwebtoken.Clock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HomeController {

    @GetMapping("/")
    public LocalDateTime home() {
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
}