package com.homeautomation.homeAutomation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private Long deviceId;

    // ex: Lights -> Living room
    private String name;

    public enum DeviceType {
        LIGHTS,
        //Washer, Fridge, Blinds...
        UTILITY,
        SPEAKER,
        TELEVISION,
        CAMERA,
    }

    private DeviceDto.DeviceType type;

    //!Might need to be Dto instead
    private GroupDto groupDto;

    private List<BehaviourDto> behaviourEntities;

    private UserDto userDto;
}
