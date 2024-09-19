package com.homeautomation.homeAutomation.controller.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//Like A DTO

public class RegisterRequest {
    private String username;
    private String password;
//    private Set<String> roles; //actually not needed, duh
}
