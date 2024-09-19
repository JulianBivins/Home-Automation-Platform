package com.homeautomation.homeAutomation.controller.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//Like A DTO
public class AuthenticationRequest {
    private String username;
    private String password;
//    private Set<String> roles; //actually not needed, duh
}
