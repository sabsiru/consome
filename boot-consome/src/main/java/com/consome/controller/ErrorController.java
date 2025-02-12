package com.consome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    // error path를 꼭 "/error" 로 하자!
    private final String ERROR_PATH = "/error";

//    @GetMapping(ERROR_PATH)
//    public String redirectRoot(){
//        return "index.html";
//    }
}
