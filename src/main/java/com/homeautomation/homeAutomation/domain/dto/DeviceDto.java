package com.homeautomation.homeAutomation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private Long deviceId;

    private String name;

    private DeviceType type;

    private UserDto userDto;

    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

    public enum DeviceType {
        LIGHTS,
        UTILITY,
        SPEAKER,
        TELEVISION,
        CAMERA,
    }
}
