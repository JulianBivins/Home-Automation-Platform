package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.*;
import com.homeautomation.homeAutomation.config.ValidationGroups;
import com.homeautomation.homeAutomation.domain.enums.DeviceType;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "deviceId",    scope = DeviceDto.class)
public class DeviceDto {

//    @NotNull(message = "Device ID cannot be null", groups = ValidationGroups.Update.class)
    private Long deviceId;

    @NotBlank(message = "Device name is mandatory", groups = {ValidationGroups.Create.class
//            , ValidationGroups.Update.class
    })
    private String name;

    @NotNull(message = "Device type is mandatory", groups = ValidationGroups.Create.class)
    private DeviceType type;

    @JsonIgnore
//    @NotNull(message = "User DTO cannot be null", groups = ValidationGroups.Create.class)
    private UserDto userDto;

//    @JsonBackReference(value = "rule-devices")
    @JsonIgnore
    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

//    public enum DeviceType {
//        LIGHTS,
//        UTILITY,
//        SPEAKER,
//        TELEVISION,
//        CAMERA,
//    }
}
