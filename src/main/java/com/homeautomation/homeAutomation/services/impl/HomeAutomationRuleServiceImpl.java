package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.GroupService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.stereotype.Service;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity.Behaviour;

import javax.swing.*;
import java.util.*;

@Service("homeAutomationRuleService")//SpEL expressions
public class HomeAutomationRuleServiceImpl implements HomeAutomationRuleService {

    final private HomeAutomationRuleRepository homeAutomationRuleRepository;
    final private DeviceRepository deviceRepository;
    final private GroupRepository groupRepository;
    final private GroupService groupService;
    final private DeviceService deviceService;

    public HomeAutomationRuleServiceImpl(HomeAutomationRuleRepository homeAutomationRuleRepository, DeviceRepository deviceRepository, GroupRepository groupRepository, GroupService groupService, DeviceService deviceService) {
        this.homeAutomationRuleRepository = homeAutomationRuleRepository;
        this.deviceRepository = deviceRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.deviceService = deviceService;
    }

    @Override
    public HomeAutomationRuleEntity save(HomeAutomationRuleEntity homeAutomationRuleEntity) {
        return homeAutomationRuleRepository.save(homeAutomationRuleEntity);
    }

    @Override
    public HomeAutomationRuleEntity saveUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        homeAutomationRuleEntity.setRuleId(id);
        return homeAutomationRuleRepository.save(homeAutomationRuleEntity);
    }

    @Override
    public Iterable<HomeAutomationRuleEntity> findAll() {
        return homeAutomationRuleRepository.findAll();
    }

    @Override
    public Optional<HomeAutomationRuleEntity> findById(Long id) {
        return homeAutomationRuleRepository.findById(id);
    }

    @Override
    public List<HomeAutomationRuleEntity> getRulesByUserId(UserEntity userEntity) {
        return homeAutomationRuleRepository.findByUserEntity_UserId(userEntity.getUserId());
    }

    @Override
    public Optional<HomeAutomationRuleEntity> findByRuleName(String ruleName) {
        return homeAutomationRuleRepository.findByRuleName(ruleName);
    }


    @Override
    public HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        homeAutomationRuleEntity.setRuleId(id);

        //What happens if new groups are added that haven't been persisted. Error message in code.
            // this is only temporary. In production only one will first persist a group anyway before being able to add it to the rule
                //this code should there for become boilerplate code
        List<GroupEntity> groupEntities = homeAutomationRuleEntity.getGroupEntities();
        List<GroupEntity> updatedGroups = new ArrayList<>();
        if (groupEntities != null) {
            for (GroupEntity group : groupEntities) {
                if (group.getGroupId() == null) {
                    group.setUserEntity(homeAutomationRuleEntity.getUserEntity());
                    group = groupRepository.save(group);
                }
                updatedGroups.add(group);
            }
        }

        List<DeviceEntity> deviceEntities = homeAutomationRuleEntity.getDeviceEntities();
        List<DeviceEntity> updatedDevices = new ArrayList<>();
        if (deviceEntities != null) {
            for (DeviceEntity device : deviceEntities) {
                if (device.getDeviceId() == null) {
                    device.setUserEntity(homeAutomationRuleEntity.getUserEntity()); // Ensure user is set
                    device = deviceRepository.save(device); // Update device with saved entity
                }
                updatedDevices.add(device);
            }
        }

//        Map<Long, Behaviour> deviceBehaviours = homeAutomationRuleEntity.getDeviceBehaviours();
//        Map<Long, Behaviour> updatedBehaviours = new HashMap<>();
//        if (deviceBehaviours != null) {
//            for (Map.Entry<Long, Behaviour> entry : deviceBehaviours.entrySet()) {
//                Long deviceId = entry.getKey();
//                Behaviour behaviour = entry.getValue();
//                DeviceEntity device = deviceRepository.findById(deviceId)
//                        .orElseThrow(() -> new RuntimeException("Device not found with id " + deviceId));
//                updatedBehaviours.put(device.getDeviceId(), behaviour);
//            }
//        }



        return homeAutomationRuleRepository.findById(id).map(existingRule -> {
            Optional.ofNullable(homeAutomationRuleEntity.getRuleName()).ifPresent(existingRule::setRuleName);
            Optional.ofNullable(homeAutomationRuleEntity.getDescription()).ifPresent(existingRule::setDescription);
            if (!updatedGroups.isEmpty()) {
                for (GroupEntity group : updatedGroups) {
                    group.setRule(existingRule);
//                    groupService.partialUpdate(group.getGroupId(), group); // Saving updated group might not be necessary due to cascading
                }
                existingRule.setGroupEntities(updatedGroups);
            }
            if (!updatedDevices.isEmpty()) {
                for (DeviceEntity device : updatedDevices) {
                    List<HomeAutomationRuleEntity> rules = device.getRules();
                    rules.add(existingRule);
                    device.setRules(new ArrayList<>(rules));
//                    deviceService.partialUpdate(device.getDeviceId(), device); // Saving updated device might not be necessary due to cascading
                }
                existingRule.setDeviceEntities(updatedDevices);

            }
//            if (!updatedBehaviours.isEmpty()) existingRule.setDeviceBehaviours(new HashMap<>(updatedBehaviours));
            Map<Long, Behaviour> deviceBehaviours = homeAutomationRuleEntity.getDeviceBehaviours();
            if (deviceBehaviours != null && !deviceBehaviours.isEmpty()) {
                Map<Long, Behaviour> updatedBehaviours = new HashMap<>();
                for (Map.Entry<Long, Behaviour> entry : deviceBehaviours.entrySet()) {
                    Long deviceId = entry.getKey();
                    Behaviour behaviour = entry.getValue();
                    DeviceEntity device = deviceRepository.findById(deviceId)
                            .orElseThrow(() -> new RuntimeException("Device not found with id " + deviceId));
                    updatedBehaviours.put(device.getDeviceId(), behaviour);
                }
                // Modify the existing map instead of replacing it
                existingRule.getDeviceBehaviours().clear();
                existingRule.getDeviceBehaviours().putAll(updatedBehaviours);
            }
            return homeAutomationRuleRepository.save(existingRule);
        }).orElseThrow(() -> new RuntimeException("Rule does not exist"));
    }

//    @Override
//    public HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
//        // Find the existing rule by ID
//        return homeAutomationRuleRepository.findById(id).map(existingRule -> {
//            // Update the basic fields only if they are provided in the current entity
//            Optional.ofNullable(homeAutomationRuleEntity.getRuleName())
//                    .ifPresent(existingRule::setRuleName);
//
//            Optional.ofNullable(homeAutomationRuleEntity.getDescription())
//                    .ifPresent(existingRule::setDescription);
//
//            // Update group entities if provided; create a new mutable list to avoid issues with immutability
//            Optional.ofNullable(homeAutomationRuleEntity.getGroupEntities())
//                    .ifPresent(newGroups -> existingRule.setGroupEntities(new ArrayList<>(newGroups)));
//            Optional.ofNullable(homeAutomationRuleEntity.getEvent()).ifPresent(existingRule::setEvent);
//
//            // Save the updated existing rule entity
//            return homeAutomationRuleRepository.save(existingRule);
//        }).orElseThrow(() -> new RuntimeException("Rule does not exist"));
//    }



    @Override
    public boolean isExists(Long id) {
        return homeAutomationRuleRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        homeAutomationRuleRepository.deleteById(id);
    }

    @Override
    public void removeDeviceFromRule(Long ruleId, Long deviceId) {
        Optional<HomeAutomationRuleEntity> ruleOptional = homeAutomationRuleRepository.findById(ruleId);

        if (ruleOptional.isPresent()) {
            HomeAutomationRuleEntity rule = ruleOptional.get();

            if(!rule.getDeviceEntities().removeIf(device -> device.getDeviceId().equals(deviceId))) throw new RuntimeException("This device Id isn't associated with this particular rule");

//            homeAutomationRuleRepository.save(rule);
            partialUpdate(ruleId, rule);
        } else{
            throw new RuntimeException("Rule not found with id: " + ruleId);
        }
    }

    @Override
    public boolean isOwner(Long ruleId, String currentUsername) {
        Optional<HomeAutomationRuleEntity> rule = homeAutomationRuleRepository.findById(ruleId);
        return rule.isPresent() && rule.get().getUserEntity().getUsername().equals(currentUsername);
    }

    @Override
    public boolean isOwner(Long ruleId, Long deviceId, String currentUsername) {
        Optional<HomeAutomationRuleEntity> rule = homeAutomationRuleRepository.findById(ruleId);
        Optional<DeviceEntity> device = deviceRepository.findById(deviceId);
        return rule.isPresent() && rule.get().getUserEntity().getUsername().equals(currentUsername)
               && device.isPresent() && device.get().getRules().contains(rule.get());

    }

//    @Override
//    public List<HomeAutomationRuleEntity> findByGroupEntities_GroupId(Long groupId) {
//        return homeAutomationRuleRepository.findByGroupEntities_GroupId(groupId);
//    }




}
