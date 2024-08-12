package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    private Long groupId;

    private String name;

    private List<HomeAutomationRuleEntity> rules;

    private UserEntity userEntity;

}
