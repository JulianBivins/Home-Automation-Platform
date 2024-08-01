package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="home_automation_rules")
public class HomeAutomationRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long rule_id;

    private String ruleName;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    //!Might need to be Dto
    private UserEntity userEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    //!Might need to be Dto
    private GroupEntity groupEntity;

    //I think this is unnecessary since the devices will be give over through the rules or the behavior table anyway
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
//    @JoinColumn(name = "device_id")
//    private List<DeviceEntity> device;

    @OneToMany(mappedBy = "homeAutomationRuleEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BehaviourEntity> behaviourEntities;

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }
    private Event trigger;

}
