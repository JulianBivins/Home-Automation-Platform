package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
//import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.domain.enums.Behaviour;
import com.homeautomation.homeAutomation.mapper.Mapper;
//import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rules")
public class HomeAutomationRuleController {

    @Autowired
    private  HomeAutomationRuleService homeAutomationRuleService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private  Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> ruleMapper;
    @Autowired
    private Mapper<DeviceEntity, DeviceDto> deviceMapper;
    @Autowired
    private DeviceRepository deviceRepository;

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, authentication.name)")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ruleList")
    public ResponseEntity<List<HomeAutomationRuleDto>> listHomeAutomationRules(Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<UserEntity> retrievedUserAssociatedWithTheRule = userService.findByUsername(currentUsername);
        if (retrievedUserAssociatedWithTheRule.isEmpty()) throw new RuntimeException("User is not Present");

        List<HomeAutomationRuleEntity> returnedRules = homeAutomationRuleService.getRulesByUserId(retrievedUserAssociatedWithTheRule.get());

        List<HomeAutomationRuleDto> ruleDtos = returnedRules.stream()
                .map(ruleMapper::mapTo)
                .collect(Collectors.toList());
//        String string = ruleDtos.stream().toString();
        return new ResponseEntity<>(ruleDtos, HttpStatus.OK);
    }

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, authentication.name)")
    @GetMapping("/Event/{ruleId}")
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

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, authentication.name)")
    @GetMapping("/devices/{ruleId}")
    public ResponseEntity<List<DeviceDto>> getDeviceAssociatedWithARule(@PathVariable Long ruleId) {
        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if (retrievedDBRuleEntity.isEmpty()) throw new RuntimeException("There is no Rule associated with this Id");

        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();
        List<DeviceEntity> deviceEntities = retrievedRuleEntity.getDeviceEntities();
        List<DeviceDto> deviceDtos = deviceEntities.stream().map(deviceEntity -> deviceMapper.mapTo(deviceEntity)).toList();
        return  new ResponseEntity<>(deviceDtos, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<HomeAutomationRuleDto> createHomeAutomationRuleEntity(@RequestBody HomeAutomationRuleDto homeAutomationRuleDto, Authentication authentication) {
        String currentUsername = authentication.getName();
        UserEntity currentUser = userService.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
        HomeAutomationRuleEntity ruleEntity = ruleMapper.mapFrom(homeAutomationRuleDto);
        ruleEntity.setUserEntity(currentUser);
        HomeAutomationRuleEntity createdRuleEntity = homeAutomationRuleService.save(ruleEntity);
        return new ResponseEntity<>(ruleMapper.mapTo(createdRuleEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, authentication.name)")
    @PatchMapping("/update/{ruleId}")
    public ResponseEntity<HomeAutomationRuleDto> partialUpdateRule (@PathVariable Long ruleId, @RequestBody HomeAutomationRuleDto homeAutomationRuleDto) {

        if (!homeAutomationRuleService.isExists(ruleId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        homeAutomationRuleDto.setRuleId(ruleId);
        HomeAutomationRuleEntity updateHomeAutomationRuleEntity = ruleMapper.mapFrom(homeAutomationRuleDto);
        HomeAutomationRuleEntity savedHomeAutomationRule = homeAutomationRuleService.partialUpdate(ruleId, updateHomeAutomationRuleEntity);
        HomeAutomationRuleDto savedUpdatedHomeAutomationRuleDto = ruleMapper.mapTo(savedHomeAutomationRule);

        return new ResponseEntity<>(savedUpdatedHomeAutomationRuleDto, HttpStatus.OK);
    }

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, authentication.name)")
    @PatchMapping("/devices/add/{ruleId}")
    public ResponseEntity<List<DeviceDto>> addDevice (@PathVariable Long ruleId, @RequestBody DeviceDto deviceDto){
        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if (retrievedDBRuleEntity.isEmpty()) throw new RuntimeException("There is not Rule associated with this ruleId");
        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();

        DeviceEntity deviceEntity = deviceMapper.mapFrom(deviceDto);

        DeviceEntity toBeAddedDeviceEntity = deviceRepository.findById(deviceEntity.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found with id " + deviceEntity.getDeviceId()));

        toBeAddedDeviceEntity.getRules().add(retrievedRuleEntity);
        retrievedRuleEntity.getDeviceEntities().add(toBeAddedDeviceEntity);

        if (!retrievedRuleEntity.getDeviceEntities().contains(toBeAddedDeviceEntity)) throw new RuntimeException("The device could not be added");



        HomeAutomationRuleEntity ruleEntityAfterAddingDevice = homeAutomationRuleService.partialUpdate(ruleId, retrievedRuleEntity);


        HomeAutomationRuleEntity ruleEntityDBAfterAddingDevice = homeAutomationRuleService.findById(ruleEntityAfterAddingDevice.getRuleId())
                .orElseThrow(() -> new RuntimeException("Rule was not retrieved correctly"));

        List<DeviceDto> deviceDtos = ruleEntityDBAfterAddingDevice.getDeviceEntities().stream()
                .map(deviceMapper::mapTo)
                .toList();

        return new ResponseEntity<>(deviceDtos, HttpStatus.OK);
    }

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, #deviceId, authentication.name)")
    @PatchMapping("/{ruleId}/devices/addBehaviour/{deviceId}")
    public ResponseEntity<DeviceDto> addBehaviourToDevices(@PathVariable Long ruleId, @PathVariable Long deviceId, @RequestBody Behaviour behaviour) {

        Optional<HomeAutomationRuleEntity> retrievedDBRuleEntity = homeAutomationRuleService.findById(ruleId);
        if(retrievedDBRuleEntity.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        HomeAutomationRuleEntity retrievedRuleEntity = retrievedDBRuleEntity.get();

        Behaviour behaviourToBeAdded = Behaviour.valueOf(behaviour.toString());
        Map<Long, Behaviour> deviceBehaviours = retrievedRuleEntity.getDeviceBehaviours();
//        deviceBehaviours.put(deviceId, behaviourToBeAdded);

        Map<Long, Behaviour> deviceBehaviours2 = new HashMap<>(deviceBehaviours);
        deviceBehaviours2.put(deviceId, behaviourToBeAdded);


//        if (deviceBehaviours.containsKey(deviceId) && !deviceBehaviours.containsValue(behaviourToBeAdded)) {
//            }
//        else {System.err.println("ERROR: Unable to add the behaviour to the deviceId, behavior already exists");
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}

//        if (deviceBehaviours.containsKey(deviceId)) {
//            if (deviceBehaviours.get(deviceId).equals(behaviourToBeAdded)) {
//                // The same behaviour already exists, return OK
//                return new ResponseEntity<>(HttpStatus.OK);
//            } else {
//                //updating behaviour
//                deviceBehaviours.put(deviceId, behaviourToBeAdded);
//            }
//        } else {
//            //adding behaviour
//            deviceBehaviours.put(deviceId, behaviourToBeAdded);
//        }
//        deviceBehaviours2.putAll(deviceBehaviours);
        retrievedRuleEntity.setDeviceBehaviours(new HashMap<>(deviceBehaviours2));

    HomeAutomationRuleEntity toReturnDeviceEntity = homeAutomationRuleService.partialUpdate(ruleId, retrievedRuleEntity);

        DeviceEntity updatedDeviceEntity = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id " + deviceId));

        DeviceDto toReturnDeviceDto = deviceMapper.mapTo(updatedDeviceEntity);
        return new ResponseEntity<>(toReturnDeviceDto, HttpStatus.OK);

    }

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, authentication.name)")
    @DeleteMapping( "/delete/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long ruleId) {
        if(!homeAutomationRuleService.isExists(ruleId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        homeAutomationRuleService.delete(ruleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@homeAutomationRuleService.isOwner(#ruleId, #deviceId, authentication.name)")
    @DeleteMapping("/{ruleId}/devices/removeDevice/{deviceId}")
    public ResponseEntity<HomeAutomationRuleDto> removeDeviceFromRule(@PathVariable Long ruleId, @PathVariable Long deviceId) {
        if(!homeAutomationRuleService.isExists(ruleId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        homeAutomationRuleService.removeDeviceFromRule(ruleId, deviceId);
        HomeAutomationRuleEntity homeAutomationRuleEntity = homeAutomationRuleService.findById(ruleId).orElseThrow(() -> new RuntimeException("Rule not found"));
        HomeAutomationRuleDto homeAutomationRuleDto = ruleMapper.mapTo(homeAutomationRuleEntity);
        return new ResponseEntity<>(homeAutomationRuleDto, HttpStatus.OK);
    }



}
