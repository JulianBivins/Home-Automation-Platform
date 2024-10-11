package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.*;
import com.homeautomation.homeAutomation.domain.enums.Behaviour;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
//because of circular referencing should replace with id?
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ruleId",    scope = HomeAutomationRuleDto.class)
public class HomeAutomationRuleDto {

    @NotNull(message = "Rule ID cannot be null")
    private Long ruleId;

    @NotBlank(message = "Rule name is mandatory")
    private String ruleName;

    @NotBlank(message = "Description is mandatory")
    private String description;

//    @JsonBackReference(value = "user-rules")
    @JsonIgnore
    @NotNull(message = "User DTO cannot be null")
    private UserDto userDto;

//    @JsonManagedReference(value = "rule-groups")
//    @JsonIgnore
    private List<GroupDto> groupDtos = new ArrayList<>();

//    @JsonManagedReference(value = "rule-devices")
    private List<DeviceDto> deviceDtos = new ArrayList<>();

    @NotNull(message = "Event is mandatory")
    private Event event;

//    @JsonIgnore
    @Valid
    private Map<@NotNull(message = "Device ID cannot be null") Long, @NotNull(message = "Behaviour cannot be null") Behaviour> deviceBehaviours = new HashMap<>();

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }

//    public enum Behaviour {
//        ON,
//        OFF,
//        STAND_BY,
//        TIMED,
//    }
}
