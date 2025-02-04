package com.consome.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String home(){
        return "foward:/index.html";
    }
}
