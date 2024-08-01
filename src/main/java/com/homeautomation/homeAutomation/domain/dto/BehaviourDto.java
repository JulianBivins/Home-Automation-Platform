package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="behaviours")
public class BehaviourDto {

    private Long id;

    enum Behaviour {
        ON,
        OFF,
        STAND_BY,
        TIMED,
    }
    private String behaviour;

    private DeviceEntity deviceEntity;

    private HomeAutomationRuleEntity homeAutomationRuleEntity;

}
