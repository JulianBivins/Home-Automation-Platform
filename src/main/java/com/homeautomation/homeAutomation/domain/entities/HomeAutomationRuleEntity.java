package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name="home_automation_rules")
public class HomeAutomationRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long ruleId;

    private String ruleName;

    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "group_id")
    private GroupEntity groupEntity;

    //I think this is unnecessary since the devices will be give over through the rules or the behavior table anyway
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
//    @JoinColumn(name = "device_id")
//    private List<DeviceEntity> device;

    @OneToMany(mappedBy = "homeAutomationRuleEntity", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<BehaviourEntity> behaviourEntities;

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }
    @Enumerated(EnumType.STRING)
    private Event event;

    @Override
    public String toString() {
        return "HomeAutomationRuleEntity{" +
               "ruleId=" + ruleId +
               ", ruleName='" + ruleName + '\'' +
               ", description='" + description + '\'' +
               ", event=" + event +
               '}';
    }
}
