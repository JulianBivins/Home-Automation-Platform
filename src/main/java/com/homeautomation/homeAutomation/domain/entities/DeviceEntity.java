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
@Table(name="devices")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    // ex: Lights -> Living room
    private String name;

    public enum DeviceType {
            LIGHTS,
            //Washer, Fridge, Blinds...
            UTILITY,
            SPEAKER,
            TELEVISION,
            CAMERA,
    }

    @Enumerated(EnumType.STRING)
//    @OneToOne (mappedBy = "device_type")
    private DeviceType type;

    @ManyToOne
    @JoinColumn(name = "group_id")
    //!Might need to be Entity instead
    private GroupEntity groupEntity;

    @OneToMany(mappedBy = "deviceEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BehaviourEntity> behaviourEntities;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    //Think it is unnecessary since I already have the behaviour class defining the table
//    @ManyToMany
//    @JoinTable(
//            name = "device_rule",
//            joinColumns = @JoinColumn(name = "device_id"),
//            inverseJoinColumns = @JoinColumn(name = "rule_id")
//    )
//    private List<HomeAutomationRuleEntity> rules;
}
