//package com.homeautomation.homeAutomation.domain.dto;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class BehaviourDto {
//
//    private Long behaviourId;
//
//    public enum Behaviour {
//        ON,
//        OFF,
//        STAND_BY,
//        TIMED,
//    }
//    private Behaviour behaviour;
//
//    private DeviceDto deviceDto;
//
//    private HomeAutomationRuleDto homeAutomationRuleDto;
//
//    public BehaviourDto(Behaviour behaviour, HomeAutomationRuleDto homeAutomationRuleDto, DeviceDto deviceDto) {
//        this.behaviour = behaviour;
//        this.deviceDto = deviceDto;
//        this.homeAutomationRuleDto = homeAutomationRuleDto;
//    }
//}
