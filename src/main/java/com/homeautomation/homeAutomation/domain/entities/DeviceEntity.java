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
@Table(name="devices")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long device_Id;

    // ex: Lights -> Living room
    String name;

    public enum DeviceType {
            LIGHTS,
            //Washer, Fridge, Blinds...
            UTILITY,
            SPEAKER,
            TELEVISION,
            CAMERA,
    }

    @Enumerated(EnumType.STRING)
    @OneToOne (mappedBy = "device_type")
    private DeviceType type;

    @ManyToOne
    @JoinColumn(name = "group_id")
    //!Might need to be Dto instead
    private GroupEntity groupEntity;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BehaviourEntity> behaviourEntities;

    //Think it is unnecessary since I already have the behaviour class defining the table
//    @ManyToMany
//    @JoinTable(
//            name = "device_rule",
//            joinColumns = @JoinColumn(name = "device_id"),
//            inverseJoinColumns = @JoinColumn(name = "rule_id")
//    )
//    private List<HomeAutomationRuleEntity> rules;
}
