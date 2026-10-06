package com.example.notepad_api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello from my notepad API!";
    }


    @GetMapping("/about")
    public String about(){
        return "Omo mehn, them dey give me class activity keh";
    }
}

