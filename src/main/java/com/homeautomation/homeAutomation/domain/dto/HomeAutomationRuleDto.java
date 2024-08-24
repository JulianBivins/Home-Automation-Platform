package com.homeautomation.homeAutomation.domain.dto;

//import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private List<GroupDto> groupDtos;

    private List<DeviceDto> deviceDtos;

    private List<Event> events;

    private Map<DeviceDto, Behaviour> deviceBehaviours;

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
