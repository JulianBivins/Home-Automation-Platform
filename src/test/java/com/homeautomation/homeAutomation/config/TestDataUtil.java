package com.homeautomation.homeAutomation.config;


import com.homeautomation.homeAutomation.domain.entities.*;

import java.util.List;
import java.util.Random;

public final class TestDataUtil {

    private static Random randomEvent = new Random();
    private static Random randomBehaviour = new Random(4);
    private static Long id = 0L;

//    //TODO: CHECK IF THE CONSTRUCTORS HAVE TO ACCEPT DTOs OR ENTITIES
//     //TODO: Seems like they need to accept Dtos
//    private TestDataUtil(){
//    }
//
//    public static UserEntity createTestUserEntityA() {
//        UserEntity userEntityA = new UserEntity();
//        userEntityA.setUserId(1L);
//        userEntityA.setUsername("UserA");
//        userEntityA.setPassword("1234");
//        userEntityA.setRules(listOfRuleEntities());
//        return userEntityA;
//    }
//
//
//    public static UserEntity createTestUserEntityB() {
//        UserEntity userEntityB = new UserEntity();
//        userEntityB.setUserId(2L);
//        userEntityB.setUsername("UserB");
//        userEntityB.setPassword("5678");
//        userEntityB.setRules(
//                listOfRuleEntities()
//        );
//        return userEntityB;
//    }
//    public static UserEntity createTestUserEntityC() {
//        UserEntity userEntityC = new UserEntity();
//        userEntityC.setUserId(6L);
//        userEntityC.setUsername("UserC");
//        userEntityC.setPassword("9012");
//        userEntityC.setRules(
//                listOfRuleEntities()
//        );
//        return userEntityC;
//    }
//
//    private static HomeAutomationRuleEntity createTestRuleEntityA() {
//        HomeAutomationRuleEntity homeAutomationRuleEntity = new HomeAutomationRuleEntity();
//        homeAutomationRuleEntity.setRuleId(1L);
//        homeAutomationRuleEntity.setRuleName("RuleA");
//        homeAutomationRuleEntity.setDescription("Mock RuleA");
//        homeAutomationRuleEntity.setUserEntity(createTestUserEntityA());
//        homeAutomationRuleEntity.setGroupEntity(createGroupEntityA());
//        homeAutomationRuleEntity.setBehaviourEntities(listOfBehaviourEntities());
//        homeAutomationRuleEntity.setEvent(generateTriggerEventEntity());
//        return homeAutomationRuleEntity;
//    }
//    private static HomeAutomationRuleEntity createTestRuleEntityB() {
//        HomeAutomationRuleEntity homeAutomationRuleEntity = new HomeAutomationRuleEntity();
//        homeAutomationRuleEntity.setRuleId(2L);
//        homeAutomationRuleEntity.setRuleName("RuleB");
//        homeAutomationRuleEntity.setDescription("Mock RuleB");
//        homeAutomationRuleEntity.setUserEntity(createTestUserEntityA());
//        homeAutomationRuleEntity.setGroupEntity(createGroupEntityA());
//        homeAutomationRuleEntity.setBehaviourEntities(listOfBehaviourEntities());
//        homeAutomationRuleEntity.setEvent(generateTriggerEventEntity());
//        return homeAutomationRuleEntity;
//    }
//    private static HomeAutomationRuleEntity createTestRuleEntityC() {
//        HomeAutomationRuleEntity homeAutomationRuleEntity = new HomeAutomationRuleEntity();
//        homeAutomationRuleEntity.setRuleId(6L);
//        homeAutomationRuleEntity.setRuleName("RuleC");
//        homeAutomationRuleEntity.setDescription("Mock RuleC");
//        homeAutomationRuleEntity.setUserEntity(createTestUserEntityC());
//        homeAutomationRuleEntity.setGroupEntity(createGroupEntityA());
//        homeAutomationRuleEntity.setBehaviourEntities(listOfBehaviourEntities());
//        homeAutomationRuleEntity.setEvent(generateTriggerEventEntity());
//        return homeAutomationRuleEntity;
//    }
//
////    private static HomeAutomationRuleEntity createTestRuleEntityA() {
////        HomeAutomationRuleEntity ruleA = new HomeAutomationRuleEntity();
////        ruleA.setRule_id(1L);
////        ruleA.setRuleName("RuleA");
////        ruleA.setDescription("Mock RuleA");
////        ruleA.setUserDto(createTestUserDtoA());
////        ruleA.setGroupDto(createGroupDtoA());
////        ruleA.setBehaviourDtos(List.of(
////                generateBehaviourEntity(),
////                generateBehaviourEntity(),
////                generateBehaviourEntity()));
////        ruleA.setTrigger(generateEventDto());
////        return ruleA;
////    }
//
////    private static HomeAutomationRuleEntity createTestRuleEntityB() {
////        HomeAutomationRuleEntity ruleB = new HomeAutomationRuleEntity();
////        ruleB.setRule_id(1L);
////        ruleB.setRuleName("RuleA");
////        ruleB.setDescription("Mock RuleA");
////        ruleB.setUserEntity(createTestUserEntityA());
////        ruleB.setGroupEntity(createGroupEntityB());
////        ruleB.setBehaviourEntities(listOfBehaviourEntities());
////        ruleB.setTrigger(generateEventDto());
////        return ruleB;
////    }
////    private static HomeAutomationRuleEntity createTestRuleEntityC() {
////        HomeAutomationRuleEntity ruleC = new HomeAutomationRuleEntity();
////        ruleC.setRule_id(1L);
////        ruleC.setRuleName("RuleA");
////        ruleC.setDescription("Mock RuleA");
////        ruleC.setUserEntity(createTestUserEntityA());
////        ruleC.setGroupEntity(createGroupEntityB());
////        ruleC.setBehaviourEntities(listOfBehaviourEntities());
////        ruleC.setTrigger(generateEventDto());
////        return ruleC;
////    }
//
////    private static GroupEntity createGroupEntityA() {
////        GroupEntity groupEntityA = new GroupEntity();
////        groupEntityA.setId(1L);
////        groupEntityA.setName("LivingRoom");
////        groupEntityA.setRules(listOfRuleDtos()
////        );
////        return groupEntityA;
////    }
////    private static GroupEntity createGroupEntityB() {
////        GroupEntity groupEntityA = new GroupEntity();
////        groupEntityA.setId(2L);
////        groupEntityA.setName("Kitchen");
////        groupEntityA.setRules(getTestRuleDtoA()
////        );
////        return groupEntityA;
////    }
//
//    private static GroupEntity createGroupEntityA() {
//        GroupEntity groupEntityA = new GroupEntity();
//        groupEntityA.setGroupId(1L);
//        groupEntityA.setName("LivingRoom");
//        groupEntityA.setRules(listOfRuleEntities());
//        return groupEntityA;
//    }
//    private static GroupEntity createGroupEntityB() {
//        GroupEntity groupEntityB = new GroupEntity();
//        groupEntityB.setGroupId(2L);
//        groupEntityB.setName("Kitchen");
//        groupEntityB.setRules(listOfRuleEntities());
//        return groupEntityB;
//    }
//
//    private static DeviceEntity createDeviceEntityA() {
//        DeviceEntity deviceEntity = new DeviceEntity();
//        deviceEntity.setDeviceId(1L);
//        deviceEntity.setName("Lights_Bathroom");
//        deviceEntity.setType(DeviceEntity.DeviceType.LIGHTS);
//        deviceEntity.setGroupEntity(createGroupEntityA());
//        deviceEntity.setBehaviourEntities(listOfBehaviourEntities());
//        return deviceEntity;
//    }
//    private static DeviceEntity createDeviceEntityB() {
//        DeviceEntity deviceEntity = new DeviceEntity();
//        deviceEntity.setDeviceId(2L);
//        deviceEntity.setName("Speaker_LivingRoom");
//        deviceEntity.setType(DeviceEntity.DeviceType.SPEAKER);
//        deviceEntity.setGroupEntity(createGroupEntityA());
//        deviceEntity.setBehaviourEntities(listOfBehaviourEntities());
//        return deviceEntity;
//    }
//
//    private static BehaviourEntity generateBehaviourEntity() {
//        int randomOrdinal = randomBehaviour.nextInt(4);
//        id++;
//        BehaviourEntity behaviourEntity = new BehaviourEntity();
//        behaviourEntity.setBehaviourId(id);
//        behaviourEntity.setBehaviour(BehaviourEntity.Behaviour.values()[randomOrdinal]);
//        behaviourEntity.setDeviceEntity(createDeviceEntityA());
//        behaviourEntity.setHomeAutomationRuleEntity(createTestRuleEntityA());
//        return behaviourEntity;
//    }
//
//    private static HomeAutomationRuleEntity.Event generateTriggerEventEntity() {
//       int randomOrdinal = randomEvent.nextInt(6);
//       return HomeAutomationRuleEntity.Event.values()[randomOrdinal];
//    }
//
//    private static List<BehaviourEntity> listOfBehaviourEntities() {
//        return List.of(generateBehaviourEntity(),
//                generateBehaviourEntity(),
//                generateBehaviourEntity());
//    }
//
//
//    private static List<HomeAutomationRuleEntity> listOfRuleEntities() {
//        return List.of(
//                createTestRuleEntityA(),
//                createTestRuleEntityB(),
//                createTestRuleEntityC()
//        );
//    }



    private TestDataUtil() {
    }

    public static UserEntity createTestUserEntityA() {
        UserEntity userEntityA = new UserEntity();
        userEntityA.setUserId(1L);
        userEntityA.setUsername("testuser");
        userEntityA.setPassword("1234");
        userEntityA.setRules(manuallyCreateRuleEntities(userEntityA));
        return userEntityA;
    }

    public static UserEntity createTestUserEntityB() {
        UserEntity userEntityB = new UserEntity();
        userEntityB.setUserId(2L);
        userEntityB.setUsername("testuser");
        userEntityB.setPassword("5678");
        userEntityB.setRules(manuallyCreateRuleEntities(userEntityB));
        return userEntityB;
    }

    public static UserEntity createTestUserEntityC() {
        UserEntity userEntityC = new UserEntity();
        userEntityC.setUserId(6L);
        userEntityC.setUsername("testuser");
        userEntityC.setPassword("9012");
        userEntityC.setRules(manuallyCreateRuleEntities(userEntityC));
        return userEntityC;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityA(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityA = new HomeAutomationRuleEntity();
//        ruleEntityA.setRuleId(1L);
        ruleEntityA.setRuleName("RuleA");
        ruleEntityA.setDescription("Mock RuleA");
        ruleEntityA.setUserEntity(userEntity);
        ruleEntityA.setGroupEntity(groupEntity);
        ruleEntityA.setBehaviourEntities(manuallyCreateBehaviourEntities(ruleEntityA, groupEntity));
        ruleEntityA.setEvent(HomeAutomationRuleEntity.Event.TIME);
        return ruleEntityA;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityB(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityB = new HomeAutomationRuleEntity();
//        ruleEntityB.setRuleId(2L);
        ruleEntityB.setRuleName("RuleB");
        ruleEntityB.setDescription("Mock RuleB");
        ruleEntityB.setUserEntity(userEntity);
        ruleEntityB.setGroupEntity(groupEntity);
        ruleEntityB.setBehaviourEntities(manuallyCreateBehaviourEntities(ruleEntityB, groupEntity));
        ruleEntityB.setEvent(HomeAutomationRuleEntity.Event.PERIOD);
        return ruleEntityB;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityC(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityC = new HomeAutomationRuleEntity();
//        ruleEntityC.setRuleId(6L);
        ruleEntityC.setRuleName("RuleC");
        ruleEntityC.setDescription("Mock RuleC");
        ruleEntityC.setUserEntity(userEntity);
        ruleEntityC.setGroupEntity(groupEntity);
        ruleEntityC.setBehaviourEntities(manuallyCreateBehaviourEntities(ruleEntityC, groupEntity));
        ruleEntityC.setEvent(HomeAutomationRuleEntity.Event.AFTER_OTHER);
        return ruleEntityC;
    }

    public static GroupEntity createGroupEntityA(UserEntity userEntity) {
        GroupEntity groupEntityA = new GroupEntity();
//        groupEntityA.setGroupId(1L);
        groupEntityA.setName("LivingRoom");
        groupEntityA.setUserEntity(userEntity);
        return groupEntityA;
    }

    public static GroupEntity createGroupEntityB(UserEntity userEntity) {
        GroupEntity groupEntityB = new GroupEntity();
//        groupEntityB.setGroupId(2L);
        groupEntityB.setName("Kitchen");
        groupEntityB.setUserEntity(userEntity);
        return groupEntityB;
    }



    //TODO: DELETE THIS METHOD LATER
    public static DeviceEntity createDeviceEntityA(GroupEntity groupEntity) {
        DeviceEntity deviceEntityA = new DeviceEntity();
//        deviceEntityA.setDeviceId(1L);
        deviceEntityA.setName("Lights_Bathroom");
        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceEntityA.setGroupEntity(groupEntity);
        return deviceEntityA;
    }
    //TODO: DELETE THIS METHOD LATER
    public static DeviceEntity createDeviceEntityB( GroupEntity groupEntity) {
        DeviceEntity deviceEntityB = new DeviceEntity();
//        deviceEntityB.setDeviceId(2L);
        deviceEntityB.setName("Speaker_LivingRoom");
        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceEntityB.setGroupEntity(groupEntity);
        return deviceEntityB;
    }

    public static DeviceEntity createDeviceEntityA(UserEntity userEntity, GroupEntity groupEntity) {
        DeviceEntity deviceEntityA = new DeviceEntity();
//        deviceEntityA.setDeviceId(1L);
        deviceEntityA.setName("Lights_Bathroom");
        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceEntityA.setGroupEntity(groupEntity);
        deviceEntityA.setUserEntity(userEntity);
        return deviceEntityA;
    }

    public static DeviceEntity createDeviceEntityB(UserEntity userEntity, GroupEntity groupEntity) {
        DeviceEntity deviceEntityB = new DeviceEntity();
//        deviceEntityB.setDeviceId(2L);
        deviceEntityB.setName("Speaker_LivingRoom");
        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceEntityB.setGroupEntity(groupEntity);
        deviceEntityB.setUserEntity(userEntity);
        return deviceEntityB;
    }

    public static BehaviourEntity generateBehaviourEntity(HomeAutomationRuleEntity ruleEntity, DeviceEntity deviceEntity) {
        int randomOrdinal = randomBehaviour.nextInt(4);
        id++;
        BehaviourEntity behaviourEntity = new BehaviourEntity();
        behaviourEntity.setBehaviourId(id);
        behaviourEntity.setBehaviour(BehaviourEntity.Behaviour.values()[randomOrdinal]);
        behaviourEntity.setDeviceEntity(deviceEntity);
        behaviourEntity.setHomeAutomationRuleEntity(ruleEntity);
        return behaviourEntity;
    }

//    public static HomeAutomationRuleEntity.Event generateTriggerEventEntity() {
//        int randomOrdinal = randomEvent.nextInt(6);
//        return HomeAutomationRuleEntity.Event.values()[randomOrdinal];
//    }

    public static List<BehaviourEntity> manuallyCreateBehaviourEntities(HomeAutomationRuleEntity ruleEntity, GroupEntity groupEntity) {
        DeviceEntity deviceEntityA = createDeviceEntityA(groupEntity);
        DeviceEntity deviceEntityB = createDeviceEntityB(groupEntity);
        return List.of(
                generateBehaviourEntity(ruleEntity, deviceEntityA),
                generateBehaviourEntity(ruleEntity, deviceEntityB)
        );
    }

    public static List<HomeAutomationRuleEntity> manuallyCreateRuleEntities(UserEntity userEntity) {
        GroupEntity groupEntity = createGroupEntityA(userEntity);
        return List.of(
                createTestRuleEntityA(userEntity, groupEntity),
                createTestRuleEntityB(userEntity, groupEntity),
                createTestRuleEntityC(userEntity, groupEntity)
        );
    }
}

