package com.assesment.simple_project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/")
    public String greetAllUsers(){
        return "Hello there, how are you?";
    }
}
