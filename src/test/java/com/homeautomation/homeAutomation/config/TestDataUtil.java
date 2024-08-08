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
        return new UserDto(
              1L, "UserDtoA", listOfRuleDtos());
    }
    public static UserDto createTestUserDtoB() {
        return new UserDto(
                2L, "UserDtoB", listOfRuleDtos());
    }
    public static UserDto createTestUserDtoC() {
        var l = listOfRuleDtos();
        return new UserDto(
                6L, "UserDtoC", listOfRuleDtos());
    }

    private static HomeAutomationRuleDto createTestRuleDtoA() {
        return new HomeAutomationRuleDto(
                1L,
                "RuleA",
                "Mock RuleA",
                createTestUserDtoA(),
                createGroupDtoA(),
                listOfBehaviourDtos(),
                generateEventDto());
    }



    private static HomeAutomationRuleDto createTestRuleDtoB() {
        return new HomeAutomationRuleDto(
                2L,
                "RuleB",
                "Mock RuleB",
                createTestUserDtoB(),
                createGroupDtoB(),
                listOfBehaviourDtos(),
                generateEventDto());
    }
    private static HomeAutomationRuleDto createTestRuleDtoC() {
        return new HomeAutomationRuleDto(
                6L,
                "RuleC",
                "Mock RuleC",
                createTestUserDtoA(),
                createGroupDtoA(),
                listOfBehaviourDtos(),
                generateEventDto());
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
        return new DeviceDto(1L,
                "Lights_Bathroom",
                DeviceEntity.DeviceType.LIGHTS,
                createGroupDtoA(),
                List.of(generateBehaviourEntity(),
                        generateBehaviourEntity()));
    }

    private static BehaviourDto generateBehaviourEntity() {
        int randomOrdinal = randomBehaviour.nextInt(4);
        id++;
        return new BehaviourDto(
//                1L,
                id,
                BehaviourDto.Behaviour.values()[randomOrdinal],
                createDeviceDtoA(),
                createTestRuleDtoA()
                );
    }
    
    private static HomeAutomationRuleDto.Event generateEventDto() {
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

