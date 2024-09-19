package com.homeautomation.homeAutomation.controller.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//This is a DTO that carries the response
public class AuthenticationResponse {
    private String token;
}
