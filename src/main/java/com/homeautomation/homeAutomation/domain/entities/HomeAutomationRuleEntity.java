package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="home_automation_rule")
public class HomeAutomationRuleEntity {



    enum Event {
        AUTO, // aka Time sensitive
        BEFORE_OTHER,
        AFTER_OTHER

    }

    @Id
    private String rule_id;

    private String ruleName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "device_id")
    private List<DeviceEntity> device;

    private Event trigger;

}
