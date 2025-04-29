package com.hustict.aims.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Xin chào từ AIMS backend 🚀";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
