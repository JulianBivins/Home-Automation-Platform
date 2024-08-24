//package com.homeautomation.homeAutomation.domain.entities;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
////@EqualsAndHashCode
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name="behaviour_rules_devices")
//public class BehaviourEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long behaviourId;
//
//     public enum Behaviour {
//        ON,
//        OFF,
//        STAND_BY,
//        TIMED,
//    }
//    @Enumerated(EnumType.STRING)
//    private Behaviour behaviour;
//
//    @ManyToOne
//    @JoinColumn(name = "device_id")
//    private DeviceEntity deviceEntity;
//
//    @ManyToOne
//    @JoinColumn(name = "rule_id")
//    private HomeAutomationRuleEntity homeAutomationRuleEntity;
//
//    public BehaviourEntity(Behaviour behaviour, HomeAutomationRuleEntity homeAutomationRuleEntity, DeviceEntity deviceEntity) {
//        this.behaviour = behaviour;
//        this.deviceEntity = deviceEntity;
//        this.homeAutomationRuleEntity = homeAutomationRuleEntity;
//    }
//}
