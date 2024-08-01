package com.homeautomation.homeAutomation.domain.entities;

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
    private Long id;

     enum Behaviour {
        ON,
        OFF,
        STAND_BY,
        TIMED,
    }
    @Enumerated(EnumType.STRING)
    private String behaviour;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private DeviceEntity deviceEntity;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private HomeAutomationRuleEntity homeAutomationRuleEntity;

}
