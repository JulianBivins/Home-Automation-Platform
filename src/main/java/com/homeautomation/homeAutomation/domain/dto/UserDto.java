package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
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

    private List<HomeAutomationRuleEntity> rules;

    public UserDto(Long l, String userEntityB, List<HomeAutomationRuleEntity> homeAutomationRuleEntitys) {

    }
}
