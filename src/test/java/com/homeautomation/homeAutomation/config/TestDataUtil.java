package com.homeautomation.homeAutomation.config;


import com.homeautomation.homeAutomation.domain.entities.*;

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
        ruleEntityA.setGroupEntity(groupEntity);
        ruleEntityA.setEvent(HomeAutomationRuleEntity.Event.TIME);
        ruleEntityA.setBehaviourEntities(manuallyCreateBehaviourEntities(ruleEntityA, groupEntity));
        return ruleEntityA;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityB(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityB = new HomeAutomationRuleEntity();
        ruleEntityB.setRuleName("RuleB");
        ruleEntityB.setDescription("Mock RuleB");
        ruleEntityB.setUserEntity(userEntity);
        ruleEntityB.setGroupEntity(groupEntity);
        ruleEntityB.setEvent(HomeAutomationRuleEntity.Event.PERIOD);
        ruleEntityB.setBehaviourEntities(manuallyCreateBehaviourEntities(ruleEntityB, groupEntity));
        return ruleEntityB;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityC(UserEntity userEntity, GroupEntity groupEntity) {
        HomeAutomationRuleEntity ruleEntityC = new HomeAutomationRuleEntity();
        ruleEntityC.setRuleName("RuleC");
        ruleEntityC.setDescription("Mock RuleC");
        ruleEntityC.setUserEntity(userEntity);
        ruleEntityC.setGroupEntity(groupEntity);
        ruleEntityC.setEvent(HomeAutomationRuleEntity.Event.AFTER_OTHER);
        ruleEntityC.setBehaviourEntities(manuallyCreateBehaviourEntities(ruleEntityC, groupEntity));
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



    //TODO: DELETE THIS METHOD LATER
    public static DeviceEntity createDeviceEntityA(GroupEntity groupEntity) {
        DeviceEntity deviceEntityA = new DeviceEntity();
        deviceEntityA.setName("Lights_Bathroom");
        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceEntityA.setGroupEntity(groupEntity);
        return deviceEntityA;
    }
    //TODO: DELETE THIS METHOD LATER
    public static DeviceEntity createDeviceEntityB( GroupEntity groupEntity) {
        DeviceEntity deviceEntityB = new DeviceEntity();
        deviceEntityB.setName("Speaker_LivingRoom");
        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceEntityB.setGroupEntity(groupEntity);
        return deviceEntityB;
    }

    public static DeviceEntity createDeviceEntityA(UserEntity userEntity, GroupEntity groupEntity) {
        DeviceEntity deviceEntityA = new DeviceEntity();
        deviceEntityA.setName("Lights_Bathroom");
        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceEntityA.setGroupEntity(groupEntity);
        deviceEntityA.setUserEntity(userEntity);
        return deviceEntityA;
    }

    public static DeviceEntity createDeviceEntityB(UserEntity userEntity, GroupEntity groupEntity) {
        DeviceEntity deviceEntityB = new DeviceEntity();
        deviceEntityB.setName("Speaker_LivingRoom");
        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceEntityB.setGroupEntity(groupEntity);
        deviceEntityB.setUserEntity(userEntity);
        return deviceEntityB;
    }

    public static BehaviourEntity generateBehaviourEntity(HomeAutomationRuleEntity ruleEntity, DeviceEntity deviceEntity) {
        int randomOrdinal = randomBehaviour.nextInt(4);
        BehaviourEntity behaviourEntity = new BehaviourEntity();
        behaviourEntity.setBehaviour(BehaviourEntity.Behaviour.values()[randomOrdinal]);
        behaviourEntity.setDeviceEntity(deviceEntity);
        behaviourEntity.setHomeAutomationRuleEntity(ruleEntity);
        return behaviourEntity;
    }

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

