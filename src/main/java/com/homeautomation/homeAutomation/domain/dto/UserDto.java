package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    Long userId;

    String username;

    String password;

    private List<HomeAutomationRuleDto> rules = new ArrayList<>();
    private List<GroupEntity> groups = new ArrayList<>();


    public UserDto(Long l, String userEntityB, List<HomeAutomationRuleDto> homeAutomationRuleEntities) {

    }
}
