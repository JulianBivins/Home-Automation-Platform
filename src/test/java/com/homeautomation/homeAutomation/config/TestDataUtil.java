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
        userEntityA.setRules(listOfRuleDtos());
        return userEntityA;
    }


    public static UserEntity createTestUserEntityB() {
        UserEntity userEntityB = new UserEntity();
        userEntityB.setUser_Id(2L);
        userEntityB.setUsername("UserB");
        userEntityB.setPassword("5678");
        userEntityB.setRules(
                listOfRuleDtos()
        );
        return userEntityB;
    }
    public static UserEntity createTestUserEntityC() {
        UserEntity userEntityC = new UserEntity();
        userEntityC.setUser_Id(6L);
        userEntityC.setUsername("UserC");
        userEntityC.setPassword("9012");
        userEntityC.setRules(
                listOfRuleDtos()
        );
        return userEntityC;
    }

    public static UserDto createTestUserDtoA() {
        UserDto userDto = new UserDto();
        userDto.setUserId(1L);
        userDto.setUsername("UserDtoA");
        userDto.setRules(listOfRuleDtos());
        return userDto;
    }
    public static UserDto createTestUserDtoB() {
        UserDto userDto = new UserDto();
        userDto.setUserId(2L);
        userDto.setUsername("UserDtoB");
        userDto.setRules(listOfRuleDtos());
        return userDto;
    }
    public static UserDto createTestUserDtoC() {
        UserDto userDto = new UserDto();
        userDto.setUserId(6L);
        userDto.setUsername("UserDtoC");
        userDto.setRules(listOfRuleDtos());
        return userDto;
    }

    private static HomeAutomationRuleDto createTestRuleDtoA() {
        HomeAutomationRuleDto homeAutomationRuleDto = new HomeAutomationRuleDto();
        homeAutomationRuleDto.setRule_id(1L);
        homeAutomationRuleDto.setRuleName("RuleA");
        homeAutomationRuleDto.setDescription("Mock RuleA");
        homeAutomationRuleDto.setUserDto(createTestUserDtoA());
        homeAutomationRuleDto.setGroupDto(createGroupDtoA());
        homeAutomationRuleDto.setBehaviourDtos(listOfBehaviourDtos());
        homeAutomationRuleDto.setTrigger(generateTriggerEventDto());
        return homeAutomationRuleDto;
    }
    private static HomeAutomationRuleDto createTestRuleDtoB() {
        HomeAutomationRuleDto homeAutomationRuleDto = new HomeAutomationRuleDto();
        homeAutomationRuleDto.setRule_id(2L);
        homeAutomationRuleDto.setRuleName("RuleB");
        homeAutomationRuleDto.setDescription("Mock RuleB");
        homeAutomationRuleDto.setUserDto(createTestUserDtoA());
        homeAutomationRuleDto.setGroupDto(createGroupDtoA());
        homeAutomationRuleDto.setBehaviourDtos(listOfBehaviourDtos());
        homeAutomationRuleDto.setTrigger(generateTriggerEventDto());
        return homeAutomationRuleDto;
    }
    private static HomeAutomationRuleDto createTestRuleDtoC() {
        HomeAutomationRuleDto homeAutomationRuleDto = new HomeAutomationRuleDto();
        homeAutomationRuleDto.setRule_id(6L);
        homeAutomationRuleDto.setRuleName("RuleC");
        homeAutomationRuleDto.setDescription("Mock RuleC");
        homeAutomationRuleDto.setUserDto(createTestUserDtoA());
        homeAutomationRuleDto.setGroupDto(createGroupDtoA());
        homeAutomationRuleDto.setBehaviourDtos(listOfBehaviourDtos());
        homeAutomationRuleDto.setTrigger(generateTriggerEventDto());
        return homeAutomationRuleDto;
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

    private static GroupDto createGroupDtoA() {
        GroupDto groupDtoA = new GroupDto();
        groupDtoA.setId(1L);
        groupDtoA.setName("LivingRoom");
        groupDtoA.setRules(listOfRuleDtos());
        return groupDtoA;
    }
    private static GroupDto createGroupDtoB() {
        GroupDto groupDtoB = new GroupDto();
        groupDtoB.setId(2L);
        groupDtoB.setName("Kitchen");
        groupDtoB.setRules(listOfRuleDtos());
        return groupDtoB;
    }

    private static DeviceDto createDeviceDtoA() {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDevice_Id(1L);
        deviceDto.setName("Lights_Bathroom");
        deviceDto.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceDto.setGroupDto(createGroupDtoA());
        deviceDto.setBehaviourDtos(listOfBehaviourDtos());
        return deviceDto;
    }
    private static DeviceDto createDeviceDtoB() {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDevice_Id(2L);
        deviceDto.setName("Speaker_LivingRoom");
        deviceDto.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceDto.setGroupDto(createGroupDtoA());
        deviceDto.setBehaviourDtos(listOfBehaviourDtos());
        return deviceDto;
    }

    private static BehaviourDto generateBehaviourEntity() {
        int randomOrdinal = randomBehaviour.nextInt(4);
        id++;
        BehaviourDto behaviourDto = new BehaviourDto();
        behaviourDto.setBehaviourId(id);
        behaviourDto.setBehaviour(BehaviourDto.Behaviour.values()[randomOrdinal]);
        behaviourDto.setDeviceDto(createDeviceDtoA());
        behaviourDto.setHomeAutomationRuleDto(createTestRuleDtoA());
        return behaviourDto;
    }
    
    private static HomeAutomationRuleDto.Event generateTriggerEventDto() {
       int randomOrdinal = randomEvent.nextInt(6);
       return HomeAutomationRuleDto.Event.values()[randomOrdinal];
    }

    private static List<BehaviourDto> listOfBehaviourDtos() {
        return List.of(generateBehaviourEntity(),
                generateBehaviourEntity(),
                generateBehaviourEntity());
    }


    private static List<HomeAutomationRuleDto> listOfRuleDtos() {
        return List.of(
                createTestRuleDtoA(),
                createTestRuleDtoB(),
                createTestRuleDtoC()
        );
    }

}

