package com.homeautomation.homeAutomation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    Long userId;

    String username;

    String password;

    private List<HomeAutomationRuleDto> rules;

    public UserDto(Long l, String userEntityB, List<HomeAutomationRuleDto> homeAutomationRuleEntities) {

    }
}
