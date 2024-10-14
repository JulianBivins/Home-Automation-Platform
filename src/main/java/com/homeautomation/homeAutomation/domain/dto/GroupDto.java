package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.homeautomation.homeAutomation.config.ValidationGroups;
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

    //need to check if this is what is most logical, because if i only pass the value to be updated, there will naturally not be any id to be replaced
//    @NotNull(message = "Group ID cannot be null", groups = ValidationGroups.Update.class)
    private Long groupId;

    @NotBlank(message = "Group name is mandatory", groups = {ValidationGroups.Create.class
//            , ValidationGroups.Update.class
    })
    private String name;

//    @JsonBackReference(value = "rule-groups")
//    @JsonIgnore
    @JsonIgnore
    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

    @JsonIgnore
//    @NotNull(message = "User DTO cannot be null", groups = ValidationGroups.Create.class)
    private UserDto userDto;

}
