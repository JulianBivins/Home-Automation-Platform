package com.homeautomation.homeAutomation.domain.entities;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="behaviours")
public class BehaviourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long behaviourId;

     public enum Behaviour {
        ON,
        OFF,
        STAND_BY,
        TIMED,
    }
    @Enumerated(EnumType.STRING)
    private Behaviour behaviour;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private DeviceDto deviceDto;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private HomeAutomationRuleDto homeAutomationRuleDto;

    public BehaviourEntity(Behaviour behaviour, HomeAutomationRuleDto homeAutomationRuleDto, DeviceDto deviceDto) {
        this.behaviour = behaviour;
        this.deviceDto = deviceDto;
        this.homeAutomationRuleDto = homeAutomationRuleDto;
    }
}
