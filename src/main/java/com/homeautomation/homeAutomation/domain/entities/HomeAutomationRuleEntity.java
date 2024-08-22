package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

//    @ManyToOne(cascade = {
////            CascadeType.PERSIST,
//            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
//    @JoinColumn(name = "group_id")
//    private GroupEntity groupEntity;

    @OneToMany(mappedBy = "rule", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<GroupEntity> groupEntities = new ArrayList<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeAutomationRuleEntity that = (HomeAutomationRuleEntity) o;
        return Objects.equals(ruleId, that.ruleId) &&
               Objects.equals(ruleName, that.ruleName) &&
               Objects.equals(description, that.description) &&
               event == that.event;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, ruleName, description, event);
    }


}
