package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
//because of circular refrencing should replace with id?
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ruleId",    scope = HomeAutomationRuleDto.class)
public class HomeAutomationRuleDto {

    private Long ruleId;

    private String ruleName;

    private String description;

//    @JsonBackReference(value = "user-rules")
    private UserDto userDto;

//    @JsonManagedReference(value = "rule-groups")
//    @JsonIgnore
    private List<GroupDto> groupDtos = new ArrayList<>();

//    @JsonManagedReference(value = "rule-devices")
    private List<DeviceDto> deviceDtos = new ArrayList<>();

    private Event event;

//    @JsonIgnore
    private Map<Long, Behaviour> deviceBehaviours = new HashMap<>();

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }

    public enum Behaviour {
        ON,
        OFF,
        STAND_BY,
        TIMED,
    }
}
