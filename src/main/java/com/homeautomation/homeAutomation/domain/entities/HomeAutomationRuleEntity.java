package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
    private List<GroupEntity> groupEntities;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "device_rule",
        joinColumns = @JoinColumn(name = "rule_id"),
        inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    private List<DeviceEntity> deviceEntities;

    @Enumerated(EnumType.STRING)
    @Column(name = "event")
    private Event event;

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }

    @MapKeyJoinColumn(name = "device_id")
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "device_id")
    @ElementCollection
    @CollectionTable(name = "rule_device_behaviour", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(name = "behaviour")
    private Map<DeviceEntity, Behaviour> deviceBehaviours;

    public enum Behaviour {
        ON,
        OFF,
        STAND_BY,
        TIMED,
    }


    @Override
    public String toString() {
        return "HomeAutomationRuleEntity{" +
               "ruleId=" + ruleId +
               ", ruleName='" + ruleName + '\'' +
               ", description='" + description + '\'' +
               ", events=" + events +
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
               Objects.equals(events, that.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, ruleName, description, events);
    }


}


