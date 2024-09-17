package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
//import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
//import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class HomeAutomationRuleController {

    @Autowired
    private  HomeAutomationRuleService homeAutomationRuleService;
    @Autowired
    private UserService userService;

    @Autowired
    private  Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> ruleMapper;
    @Autowired
    private Mapper<DeviceEntity, DeviceDto> deviceMapper;


    @GetMapping("/{ruleId}")
    public ResponseEntity<HomeAutomationRuleDto> getRuleById(@PathVariable Long ruleId, Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<UserEntity> retrievedUserAssociatedWithTheRule = userService.findByUsername(currentUsername);
        if (retrievedUserAssociatedWithTheRule.isEmpty()) throw new RuntimeException("User is not Present");

        List<HomeAutomationRuleEntity> returnedRules = homeAutomationRuleService.getRulesByUserId(retrievedUserAssociatedWithTheRule.get());
        return returnedRules.stream()
                .filter(ruleEntity -> ruleEntity.getRuleId().equals(ruleId))
                .findFirst()
                .map(ruleEntity -> {
                    HomeAutomationRuleDto homeAutomationRuleDto = ruleMapper.mapTo(ruleEntity);
                    return new ResponseEntity<>(homeAutomationRuleDto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<HomeAutomationRuleDto>> listHomeAutomationRules(Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<UserEntity> retrievedUserAssociatedWithTheRule = userService.findByUsername(currentUsername);
        if (retrievedUserAssociatedWithTheRule.isEmpty()) throw new RuntimeException("User is not Present");

        List<HomeAutomationRuleEntity> returnedRules = homeAutomationRuleService.getRulesByUserId(retrievedUserAssociatedWithTheRule.get());

        List<HomeAutomationRuleDto> ruleDtos = returnedRules.stream()
                .map(ruleMapper::mapTo)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ruleDtos, HttpStatus.OK);
    }

//    @GetMapping("/rules/{ruleId}/behaviours")
//    public ResponseEntity<ArrayList<String>> retrieveBehavioursByRule (@RequestBody HomeAutomationRuleDto homeAutomationRuleDto) {
//
//        return new ResponseEntity<>(behaviourArray, HttpStatus.OK);
//    }

    @GetMapping("/rules/{ruleId}/Events")
    public ResponseEntity<String> getEventFromRule(@PathVariable Long ruleId) {
        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if(retrievedDBRuleEntity.isEmpty()) throw new RuntimeException("There is no Rule associated with this ruleId");

        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();
        HomeAutomationRuleDto homeAutomationRuleDto = ruleMapper.mapTo(retrievedRuleEntity);

        if (homeAutomationRuleDto.getEvent() == null) throw new RuntimeException("There is no associated Event with this Rule");
        HomeAutomationRuleDto.Event associatedEvent = homeAutomationRuleDto.getEvent();
        String associatedEventString = associatedEvent.toString();
        return new ResponseEntity<>(associatedEventString, HttpStatus.OK);
    }

    @GetMapping("/rules/devices")
    public ResponseEntity<List<DeviceDto>> getDeviceAssociatedWithARule(@PathVariable Long ruleId) {
        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if (retrievedDBRuleEntity.isEmpty()) throw new RuntimeException("There is no Rule associated with this Id");

        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();
        List<DeviceEntity> deviceEntities = retrievedRuleEntity.getDeviceEntities();
        List<DeviceDto> deviceDtos = deviceEntities.stream().map(deviceEntity -> deviceMapper.mapTo(deviceEntity)).toList();
        return  new ResponseEntity<>(deviceDtos, HttpStatus.OK);
    }


    @PostMapping("/rules")
    public ResponseEntity<HomeAutomationRuleDto> createHomeAutomationRuleEntity(@RequestBody HomeAutomationRuleDto homeAutomationRuleDto) {
        HomeAutomationRuleEntity ruleEntity = ruleMapper.mapFrom(homeAutomationRuleDto);
        HomeAutomationRuleEntity createdRuleEntity = homeAutomationRuleService.save(ruleEntity);
        return new ResponseEntity<>(ruleMapper.mapTo(createdRuleEntity), HttpStatus.OK);
    }

    @PostMapping("/rules/{ruleId}")
    public ResponseEntity<HomeAutomationRuleDto> updateFullRule (@PathVariable Long ruleId, @RequestBody HomeAutomationRuleDto homeAutomationRuleDto) {

        if (!homeAutomationRuleService.isExists(ruleId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        homeAutomationRuleDto.setRuleId(ruleId);
        HomeAutomationRuleEntity updateHomeAutomationRuleEntity = ruleMapper.mapFrom(homeAutomationRuleDto);
        HomeAutomationRuleEntity savedHomeAutomationRule = homeAutomationRuleService.saveUpdate(ruleId, updateHomeAutomationRuleEntity);
        HomeAutomationRuleDto savedUpdatedHomeAutomationRuleDto = ruleMapper.mapTo(savedHomeAutomationRule);

        return new ResponseEntity<>(savedUpdatedHomeAutomationRuleDto, HttpStatus.OK);
    }

    @PatchMapping("rules/{ruleId}/devices")
    public ResponseEntity<HomeAutomationRuleDto> addDevice (@PathVariable Long ruleId, @RequestBody DeviceDto deviceDto){
        //Device id will be generated for the specific homeautomationrule
        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if (retrievedDBRuleEntity.isEmpty()) throw new RuntimeException("There is not Rule associated with this ruleId");
        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();


        List<DeviceEntity> deviceEntities = retrievedRuleEntity.getDeviceEntities();
        DeviceEntity toBeAddedDeviceEntity = deviceMapper.mapFrom(deviceDto);
        deviceEntities.add(toBeAddedDeviceEntity);
        if (!retrievedRuleEntity.getDeviceEntities().contains(toBeAddedDeviceEntity)) throw new RuntimeException("The device could not be added");

        HomeAutomationRuleEntity ruleEntityAfterAddingDevice = homeAutomationRuleService.partialUpdate(ruleId, retrievedRuleEntity);
        HomeAutomationRuleDto ruleDtoAfterAddingDevice = ruleMapper.mapTo(ruleEntityAfterAddingDevice);
        return new ResponseEntity<>(ruleDtoAfterAddingDevice, HttpStatus.OK);
    }

    @PatchMapping("/rules/{ruleId}/devices/{deviceId}/addBehaviours")
    public ResponseEntity<String> addBehaviourToDevices(@PathVariable Long ruleId, @PathVariable Long deviceId, @RequestBody HomeAutomationRuleDto.Behaviour behaviour) {
        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if(retrievedDBRuleEntity.isEmpty()) return new ResponseEntity<>("No Rule associated with this ruleId", HttpStatus.NOT_FOUND);


        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();
        HomeAutomationRuleDto homeAutomationRuleDto = ruleMapper.mapTo(retrievedRuleEntity);

        Map<Long, HomeAutomationRuleDto.Behaviour> deviceBehaviours = homeAutomationRuleDto.getDeviceBehaviours();
        deviceBehaviours.put(deviceId, behaviour);
        if (!deviceBehaviours.containsKey(deviceId) && !deviceBehaviours.containsValue(behaviour)) return new ResponseEntity<>("ERROR: Unable to add the behaviour to the deviceId, behavior already exists", HttpStatus.BAD_REQUEST);

        homeAutomationRuleService.partialUpdate(ruleId, retrievedRuleEntity);

        String associatedBehaviourString = behaviour.toString();
        return new ResponseEntity<>(associatedBehaviourString, HttpStatus.OK);
    }


    @DeleteMapping( "/rules/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        if(!homeAutomationRuleService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        homeAutomationRuleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{ruleId}/devices/{deviceId}")
    public ResponseEntity<Void> removeDeviceFromRule(@PathVariable Long ruleId, @PathVariable Long deviceId) {
        homeAutomationRuleService.removeDeviceFromRule(ruleId, deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
