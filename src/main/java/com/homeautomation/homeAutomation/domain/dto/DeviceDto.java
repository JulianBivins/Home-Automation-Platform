package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
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

    private DeviceEntity.DeviceType type;

    //!Might need to be Dto instead
    private GroupEntity groupEntity;

    private List<BehaviourEntity> behaviourEntities;

}
