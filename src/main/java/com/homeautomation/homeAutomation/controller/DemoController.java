package com.homeautomation.homeAutomation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/demo")
public class DemoController {

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @GetMapping("/demo")
    public ResponseEntity<String> sayHelloWorld(){
        return ResponseEntity.ok("Hello World");
    }
}
