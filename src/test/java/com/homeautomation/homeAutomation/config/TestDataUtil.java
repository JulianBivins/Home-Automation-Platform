package com.homeautomation.homeAutomation.config;


import com.homeautomation.homeAutomation.domain.dto.*;
import com.homeautomation.homeAutomation.domain.entities.*;

import java.util.List;
import java.util.Random;

public final class TestDataUtil {

    private static Random randomEvent = new Random();
    private static Random randomBehaviour = new Random(4);
    private static Long id = 0L;

    //TODO: CHECK IF THE CONSTRUCTORS HAVE TO ACCEPT DTOs OR ENTITIES
     //TODO: Seems like they need to accept Dtos
    private TestDataUtil(){
    }

    public static UserEntity createTestUserEntityA() {
        UserEntity userEntityA = new UserEntity();
        userEntityA.setUser_Id(1L);
        userEntityA.setUsername("UserA");
        userEntityA.setPassword("1234");
        userEntityA.setRules(listOfRuleEntitys());
        return userEntityA;
    }


    public static UserEntity createTestUserEntityB() {
        UserEntity userEntityB = new UserEntity();
        userEntityB.setUser_Id(2L);
        userEntityB.setUsername("UserB");
        userEntityB.setPassword("5678");
        userEntityB.setRules(
                listOfRuleEntitys()
        );
        return userEntityB;
    }
    public static UserEntity createTestUserEntityC() {
        UserEntity userEntityC = new UserEntity();
        userEntityC.setUser_Id(6L);
        userEntityC.setUsername("UserC");
        userEntityC.setPassword("9012");
        userEntityC.setRules(
                listOfRuleEntitys()
        );
        return userEntityC;
    }

    private static HomeAutomationRuleEntity createTestRuleEntityA() {
        HomeAutomationRuleEntity homeAutomationRuleEntity = new HomeAutomationRuleEntity();
        homeAutomationRuleEntity.setRule_id(1L);
        homeAutomationRuleEntity.setRuleName("RuleA");
        homeAutomationRuleEntity.setDescription("Mock RuleA");
        homeAutomationRuleEntity.setUserEntity(createTestUserEntityA());
        homeAutomationRuleEntity.setGroupEntity(createGroupEntityA());
        homeAutomationRuleEntity.setBehaviourEntitys(listOfBehaviourEntitys());
        homeAutomationRuleEntity.setTrigger(generateTriggerEventEntity());
        return homeAutomationRuleEntity;
    }
    private static HomeAutomationRuleEntity createTestRuleEntityB() {
        HomeAutomationRuleEntity homeAutomationRuleEntity = new HomeAutomationRuleEntity();
        homeAutomationRuleEntity.setRule_id(2L);
        homeAutomationRuleEntity.setRuleName("RuleB");
        homeAutomationRuleEntity.setDescription("Mock RuleB");
        homeAutomationRuleEntity.setUserEntity(createTestUserEntityA());
        homeAutomationRuleEntity.setGroupEntity(createGroupEntityA());
        homeAutomationRuleEntity.setBehaviourEntitys(listOfBehaviourEntitys());
        homeAutomationRuleEntity.setTrigger(generateTriggerEventEntity());
        return homeAutomationRuleEntity;
    }
    private static HomeAutomationRuleEntity createTestRuleEntityC() {
        HomeAutomationRuleEntity homeAutomationRuleEntity = new HomeAutomationRuleEntity();
        homeAutomationRuleEntity.setRule_id(6L);
        homeAutomationRuleEntity.setRuleName("RuleC");
        homeAutomationRuleEntity.setDescription("Mock RuleC");
        homeAutomationRuleEntity.setUserEntity(createTestUserEntityA());
        homeAutomationRuleEntity.setGroupEntity(createGroupEntityA());
        homeAutomationRuleEntity.setBehaviourEntitys(listOfBehaviourEntitys());
        homeAutomationRuleEntity.setTrigger(generateTriggerEventEntity());
        return homeAutomationRuleEntity;
    }

//    private static HomeAutomationRuleEntity createTestRuleEntityA() {
//        HomeAutomationRuleEntity ruleA = new HomeAutomationRuleEntity();
//        ruleA.setRule_id(1L);
//        ruleA.setRuleName("RuleA");
//        ruleA.setDescription("Mock RuleA");
//        ruleA.setUserDto(createTestUserDtoA());
//        ruleA.setGroupDto(createGroupDtoA());
//        ruleA.setBehaviourDtos(List.of(
//                generateBehaviourEntity(),
//                generateBehaviourEntity(),
//                generateBehaviourEntity()));
//        ruleA.setTrigger(generateEventDto());
//        return ruleA;
//    }

//    private static HomeAutomationRuleEntity createTestRuleEntityB() {
//        HomeAutomationRuleEntity ruleB = new HomeAutomationRuleEntity();
//        ruleB.setRule_id(1L);
//        ruleB.setRuleName("RuleA");
//        ruleB.setDescription("Mock RuleA");
//        ruleB.setUserEntity(createTestUserEntityA());
//        ruleB.setGroupEntity(createGroupEntityB());
//        ruleB.setBehaviourEntities(listOfBehaviourEntities());
//        ruleB.setTrigger(generateEventDto());
//        return ruleB;
//    }
//    private static HomeAutomationRuleEntity createTestRuleEntityC() {
//        HomeAutomationRuleEntity ruleC = new HomeAutomationRuleEntity();
//        ruleC.setRule_id(1L);
//        ruleC.setRuleName("RuleA");
//        ruleC.setDescription("Mock RuleA");
//        ruleC.setUserEntity(createTestUserEntityA());
//        ruleC.setGroupEntity(createGroupEntityB());
//        ruleC.setBehaviourEntities(listOfBehaviourEntities());
//        ruleC.setTrigger(generateEventDto());
//        return ruleC;
//    }

//    private static GroupEntity createGroupEntityA() {
//        GroupEntity groupEntityA = new GroupEntity();
//        groupEntityA.setId(1L);
//        groupEntityA.setName("LivingRoom");
//        groupEntityA.setRules(listOfRuleDtos()
//        );
//        return groupEntityA;
//    }
//    private static GroupEntity createGroupEntityB() {
//        GroupEntity groupEntityA = new GroupEntity();
//        groupEntityA.setId(2L);
//        groupEntityA.setName("Kitchen");
//        groupEntityA.setRules(getTestRuleDtoA()
//        );
//        return groupEntityA;
//    }

    private static GroupEntity createGroupEntityA() {
        GroupEntity groupEntityA = new GroupEntity();
        groupEntityA.setId(1L);
        groupEntityA.setName("LivingRoom");
        groupEntityA.setRules(listOfRuleEntitys());
        return groupEntityA;
    }
    private static GroupEntity createGroupEntityB() {
        GroupEntity groupEntityB = new GroupEntity();
        groupEntityB.setId(2L);
        groupEntityB.setName("Kitchen");
        groupEntityB.setRules(listOfRuleEntitys());
        return groupEntityB;
    }

    private static DeviceEntity createDeviceEntityA() {
        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setDevice_Id(1L);
        deviceEntity.setName("Lights_Bathroom");
        deviceEntity.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceEntity.setGroupEntity(createGroupEntityA());
        deviceEntity.setBehaviourEntitys(listOfBehaviourEntitys());
        return deviceEntity;
    }
    private static DeviceEntity createDeviceEntityB() {
        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setDevice_Id(2L);
        deviceEntity.setName("Speaker_LivingRoom");
        deviceEntity.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceEntity.setGroupEntity(createGroupEntityA());
        deviceEntity.setBehaviourEntitys(listOfBehaviourEntitys());
        return deviceEntity;
    }

    private static BehaviourEntity generateBehaviourEntity() {
        int randomOrdinal = randomBehaviour.nextInt(4);
        id++;
        BehaviourEntity behaviourEntity = new BehaviourEntity();
        behaviourEntity.setBehaviourId(id);
        behaviourEntity.setBehaviour(BehaviourEntity.Behaviour.values()[randomOrdinal]);
        behaviourEntity.setDeviceEntity(createDeviceEntityA());
        behaviourEntity.setHomeAutomationRuleEntity(createTestRuleEntityA());
        return behaviourEntity;
    }
    
    private static HomeAutomationRuleEntity.Event generateTriggerEventEntity() {
       int randomOrdinal = randomEvent.nextInt(6);
       return HomeAutomationRuleEntity.Event.values()[randomOrdinal];
    }

    private static List<BehaviourEntity> listOfBehaviourEntitys() {
        return List.of(generateBehaviourEntity(),
                generateBehaviourEntity(),
                generateBehaviourEntity());
    }


    private static List<HomeAutomationRuleEntity> listOfRuleEntitys() {
        return List.of(
                createTestRuleEntityA(),
                createTestRuleEntityB(),
                createTestRuleEntityC()
        );
    }

}

