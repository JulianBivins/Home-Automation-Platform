package com.homeautomation.homeAutomation.config;


import com.homeautomation.homeAutomation.domain.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class TestDataUtil {

    private static Random randomBehaviour = new Random(4);

    private TestDataUtil() {
    }

    public static UserEntity createTestUserEntityA() {
        UserEntity userEntityA = new UserEntity();
        userEntityA.setUsername("testuser");
        userEntityA.setPassword("1234");
        return userEntityA;
    }

    public static UserEntity createTestUserEntityB() {
        UserEntity userEntityB = new UserEntity();
        userEntityB.setUsername("testuser");
        userEntityB.setPassword("5678");
        return userEntityB;
    }

    public static UserEntity createTestUserEntityC() {
        UserEntity userEntityC = new UserEntity();
        userEntityC.setUsername("testuser");
        userEntityC.setPassword("9012");
        return userEntityC;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityA(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityA = new HomeAutomationRuleEntity();
        ruleEntityA.setRuleName("RuleA");
        ruleEntityA.setDescription("Mock RuleA");
        ruleEntityA.setUserEntity(userEntity);
        //CAUTION
        List<GroupEntity> groupEntities = new ArrayList<>();
        groupEntities.add(groupEntity);
        ruleEntityA.setGroupEntities(groupEntities);
        //
//        ruleEntityA.setEvent(HomeAutomationRuleEntity.Event.TIME);
//        ruleEntityA.setBehaviourEntities(manuallyCreateBehaviourEntities(userEntity ,ruleEntityA, be));
        return ruleEntityA;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityB(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityB = new HomeAutomationRuleEntity();
        ruleEntityB.setRuleName("RuleB");
        ruleEntityB.setDescription("Mock RuleB");
        ruleEntityB.setUserEntity(userEntity);
        //CAUTION
        List<GroupEntity> groupEntities = new ArrayList<>();
        groupEntities.add(groupEntity);
        ruleEntityB.setGroupEntities(groupEntities);
        //
//        ruleEntityB.setEvent(HomeAutomationRuleEntity.Event.PERIOD);
//        ruleEntityB.setBehaviourEntities(manuallyCreateBehaviourEntities(, ruleEntityB, groupEntity));
        return ruleEntityB;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityC(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityC = new HomeAutomationRuleEntity();
        ruleEntityC.setRuleName("RuleC");
        ruleEntityC.setDescription("Mock RuleC");
        ruleEntityC.setUserEntity(userEntity);
        //CAUTION
        List<GroupEntity> groupEntities = new ArrayList<>();
        groupEntities.add(groupEntity);
        ruleEntityC.setGroupEntities(groupEntities);
        //
//        ruleEntityC.setEvent(HomeAutomationRuleEntity.Event.AFTER_OTHER);
//        ruleEntityC.setBehaviourEntities(manuallyCreateBehaviourEntities(, ruleEntityC, groupEntity));
        return ruleEntityC;
    }

    public static GroupEntity createGroupEntityA(UserEntity userEntity) {
        GroupEntity groupEntityA = new GroupEntity();
        groupEntityA.setName("LivingRoom");
        groupEntityA.setUserEntity(userEntity);
        return groupEntityA;
    }

    public static GroupEntity createGroupEntityB(UserEntity userEntity) {
        GroupEntity groupEntityB = new GroupEntity();
        groupEntityB.setName("Kitchen");
        groupEntityB.setUserEntity(userEntity);
        return groupEntityB;
    }



//    //TODO: DELETE THIS METHOD LATER
//    public static DeviceEntity createDeviceEntityA(GroupEntity groupEntity) {
//        DeviceEntity deviceEntityA = new DeviceEntity();
//        deviceEntityA.setName("Lights_Bathroom");
//        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
////        deviceEntityA.setGroupEntity(groupEntity);
//        return deviceEntityA;
//    }
//    //TODO: DELETE THIS METHOD LATER
//    public static DeviceEntity createDeviceEntityB( GroupEntity groupEntity) {
//        DeviceEntity deviceEntityB = new DeviceEntity();
//        deviceEntityB.setName("Speaker_LivingRoom");
//        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
////        deviceEntityB.setGroupEntity(groupEntity);
//        return deviceEntityB;
//    }

    public static DeviceEntity createDeviceEntityA(UserEntity userEntity
//                                                   ,GroupEntity groupEntity,
//                                                   BehaviourEntity behaviourEntity
    ) {
        DeviceEntity deviceEntityA = new DeviceEntity();
        deviceEntityA.setName("Lights_Bathroom");
        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
//        deviceEntityA.setGroupEntity(groupEntity);
        deviceEntityA.setUserEntity(userEntity);
        return deviceEntityA;
    }

    public static DeviceEntity createDeviceEntityB(UserEntity userEntity
//                                                   ,GroupEntity groupEntity,
//                                                   BehaviourEntity behaviourEntity
    ) {
        DeviceEntity deviceEntityB = new DeviceEntity();
        deviceEntityB.setName("Speaker_LivingRoom");
        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
//        deviceEntityB.setGroupEntity(groupEntity);
//        deviceEntityB.setBehaviourEntities(new ArrayList<>(List.of(behaviourEntity)));
        deviceEntityB.setUserEntity(userEntity);
        return deviceEntityB;
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

