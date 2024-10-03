package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class HomeAutomationRuleDto {

    private Long ruleId;

    private String ruleName;

    private String description;

    private UserDto userDto;

    private List<GroupDto> groupDtos = new ArrayList<>();

    @JsonIgnore
    private List<DeviceDto> deviceDtos = new ArrayList<>();

    private Event event;

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
