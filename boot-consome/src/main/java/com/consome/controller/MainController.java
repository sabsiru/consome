package com.consome.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/main")
    public String home(){
        String message = "Hello World! Welcome to Spring Boot!";
        return message;
    }
}
