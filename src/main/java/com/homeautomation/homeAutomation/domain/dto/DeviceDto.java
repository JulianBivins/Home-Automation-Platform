package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "deviceId",    scope = DeviceDto.class)
public class DeviceDto {

    private Long deviceId;

    private String name;

    private DeviceType type;

    @JsonIgnore
    private UserDto userDto;

//    @JsonBackReference(value = "rule-devices")
    @JsonIgnore
    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

    public enum DeviceType {
        LIGHTS,
        UTILITY,
        SPEAKER,
        TELEVISION,
        CAMERA,
    }
}
