package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BehaviourDto {

    private Long behaviourId;

    public enum Behaviour {
        ON,
        OFF,
        STAND_BY,
        TIMED,
    }
    private Behaviour behaviour;

    private DeviceEntity deviceEntity;

    private HomeAutomationRuleEntity homeAutomationRuleEntity;

    public BehaviourDto(Behaviour behaviour, HomeAutomationRuleEntity homeAutomationRuleEntity, DeviceEntity deviceEntity) {
        this.behaviour = behaviour;
        this.deviceEntity = deviceEntity;
        this.homeAutomationRuleEntity = homeAutomationRuleEntity;
    }
}
