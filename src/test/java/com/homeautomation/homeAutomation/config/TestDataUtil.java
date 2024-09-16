package com.homeautomation.homeAutomation.config;

import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TestDataUtil {

    private TestDataUtil() {
    }

    public static UserEntity createTestUserEntityA() {
        UserEntity userEntityA = new UserEntity();
        userEntityA.setUsername("testuserA");
        userEntityA.setPassword("1234");
        return userEntityA;
    }

//    public static UserDto createTestUserDtoB() {
//        UserDto userDtoB = new UserDto();
//        userDtoB.setUsername("testuserB");
//        userDtoB.setPassword("5678");
//        return userDtoB;
//    }

    public static UserEntity createTestUserEntityB() {
        UserEntity userEntityB = new UserEntity();
        userEntityB.setUsername("testuserB");
        userEntityB.setPassword("5678");
        return userEntityB;
    }

    public static UserEntity createTestUserEntityC() {
        UserEntity userEntityC = new UserEntity();
        userEntityC.setUsername("testuserC");
        userEntityC.setPassword("9012");
        return userEntityC;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityA(UserEntity userEntity, GroupEntity groupEntity, List<DeviceEntity> devices) {
        HomeAutomationRuleEntity ruleEntityA = new HomeAutomationRuleEntity();
        ruleEntityA.setRuleName("RuleA");
        ruleEntityA.setDescription("Mock RuleA");
        ruleEntityA.setUserEntity(userEntity);

        List<GroupEntity> groupEntities = new ArrayList<>();
        groupEntities.add(groupEntity);
        ruleEntityA.setGroupEntities(groupEntities);

        ruleEntityA.setDeviceEntities(devices);
        Map<Long, HomeAutomationRuleEntity.Behaviour> deviceBehaviours = Map.of(
                devices.get(0).getDeviceId(), HomeAutomationRuleEntity.Behaviour.ON,
                devices.get(1).getDeviceId(), HomeAutomationRuleEntity.Behaviour.STAND_BY
        );
        ruleEntityA.setDeviceBehaviours(deviceBehaviours);

        ruleEntityA.setEvent(HomeAutomationRuleEntity.Event.TIME);

        return ruleEntityA;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityB(UserEntity userEntity, GroupEntity groupEntity, List<DeviceEntity> devices) {
        HomeAutomationRuleEntity ruleEntityB = new HomeAutomationRuleEntity();
        ruleEntityB.setRuleName("RuleB");
        ruleEntityB.setDescription("Mock RuleB");
        ruleEntityB.setUserEntity(userEntity);

        List<GroupEntity> groupEntities = new ArrayList<>();
        groupEntities.add(groupEntity);
        ruleEntityB.setGroupEntities(groupEntities);

        ruleEntityB.setDeviceEntities(devices);
        Map<Long, HomeAutomationRuleEntity.Behaviour> deviceBehaviours = Map.of(
                devices.get(0).getDeviceId(), HomeAutomationRuleEntity.Behaviour.OFF,
                devices.get(1).getDeviceId(), HomeAutomationRuleEntity.Behaviour.TIMED
        );
        ruleEntityB.setDeviceBehaviours(deviceBehaviours);

        ruleEntityB.setEvent(HomeAutomationRuleEntity.Event.PERIOD);

        return ruleEntityB;
    }

    public static HomeAutomationRuleEntity createTestRuleEntityC(UserEntity userEntity, GroupEntity groupEntity, List<DeviceEntity> devices) {
        HomeAutomationRuleEntity ruleEntityC = new HomeAutomationRuleEntity();
        ruleEntityC.setRuleName("RuleC");
        ruleEntityC.setDescription("Mock RuleC");
        ruleEntityC.setUserEntity(userEntity);

        List<GroupEntity> groupEntities = new ArrayList<>();
        groupEntities.add(groupEntity);
        ruleEntityC.setGroupEntities(groupEntities);

        ruleEntityC.setDeviceEntities(devices);
        Map<Long, HomeAutomationRuleEntity.Behaviour> deviceBehaviours = Map.of(
                devices.get(0).getDeviceId(), HomeAutomationRuleEntity.Behaviour.STAND_BY,
                devices.get(1).getDeviceId(), HomeAutomationRuleEntity.Behaviour.ON
        );
        ruleEntityC.setDeviceBehaviours(deviceBehaviours);

        ruleEntityC.setEvent(HomeAutomationRuleEntity.Event.AFTER_OTHER);

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

    public static DeviceEntity createDeviceEntityA(UserEntity userEntity) {
        DeviceEntity deviceEntityA = new DeviceEntity();
        deviceEntityA.setName("Lights_Bathroom");
        deviceEntityA.setType(DeviceEntity.DeviceType.LIGHTS);
        deviceEntityA.setUserEntity(userEntity);
        return deviceEntityA;
    }

    public static DeviceEntity createDeviceEntityB(UserEntity userEntity) {
        DeviceEntity deviceEntityB = new DeviceEntity();
        deviceEntityB.setName("Speaker_LivingRoom");
        deviceEntityB.setType(DeviceEntity.DeviceType.SPEAKER);
        deviceEntityB.setUserEntity(userEntity);
        return deviceEntityB;
    }
}