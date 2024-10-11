package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//because of circular refrencing should replace with id?
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "groupId",    scope = GroupDto.class)


public class GroupDto {

    @NotNull(message = "Group ID cannot be null")
    private Long groupId;

    @NotBlank(message = "Group name is mandatory")
    private String name;

//    @JsonBackReference(value = "rule-groups")
//    @JsonIgnore
    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

    @NotNull(message = "User DTO cannot be null")
    private UserDto userDto;

}
