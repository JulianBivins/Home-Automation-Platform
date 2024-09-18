package com.homeautomation.homeAutomation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> sayHelloWorld(){
        return ResponseEntity.ok("Hello World");
    }
}
