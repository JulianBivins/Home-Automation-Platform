package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
//import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.impl.DeviceMapperImpl;
//import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/devices")
@Validated
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceMapperImpl deviceMapper;
    @Autowired
    private HomeAutomationRuleService homeAutomationRuleService;
    @Autowired
    private UserService userService;

    @PreAuthorize("@deviceService.isOwner(#deviceId, authentication.name)")
    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long deviceId) {
        Optional<DeviceEntity> returnedDeviceEntity = deviceService.findById(deviceId);
         return returnedDeviceEntity.map(deviceEntity -> {
            DeviceDto deviceDto = deviceMapper.mapTo(deviceEntity);
            return new ResponseEntity<>(deviceDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @PreAuthorize("@deviceService.isOwner(#deviceId, authentication.name)")
//    @GetMapping("/{deviceId}/behaviours")
//    public ResponseEntity<ArrayList<Behaviour>> retrieveBehavioursByDeviceId (@PathVariable Long deviceId) {
//
//    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto, Authentication authentication) {
        String currentUsername = authentication.getName();
        UserEntity currentUser = userService.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        DeviceEntity deviceEntity = deviceMapper.mapFrom(deviceDto);
        deviceEntity.setUserEntity(currentUser);
        DeviceEntity savedDeviceEntity = deviceService.save(deviceEntity);
        return new ResponseEntity<>(deviceMapper.mapTo(savedDeviceEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("@deviceService.isOwner(#deviceId, authentication.name)")
    @PatchMapping("/update/{deviceId}")
    public ResponseEntity<DeviceDto> partialUpdate(@PathVariable Long deviceId, @RequestBody DeviceDto deviceDto) {
        if(!deviceService.isExists(deviceId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DeviceEntity deviceEntity = deviceMapper.mapFrom(deviceDto);
        DeviceEntity updatedDevice = deviceService.partialUpdate(deviceId, deviceEntity);
        return new ResponseEntity<>(
                deviceMapper.mapTo(updatedDevice),
                HttpStatus.OK);
    }

//    @PreAuthorize("@deviceService.isOwner(#deviceId, authentication.name)")
//    @DeleteMapping("/delete/rules/{deviceId}")
//    public ResponseEntity deleteDeviceOutOfRule(@PathVariable Long deviceId) {
//         if(homeAutomationRuleService.isDeviceExistsInRule(deviceId)) {
//             homeAutomationRuleService.removeDeviceFromRule(deviceId);
//             return new ResponseEntity(HttpStatus.NO_CONTENT);
//         }else {
//             return new ResponseEntity(HttpStatus.NOT_FOUND);
//         }
//    }

    @PreAuthorize("@deviceService.isOwner(#deviceId, authentication.name)")
    @DeleteMapping("/delete/{deviceId}")
    public ResponseEntity<Void> deleteDevice (@PathVariable Long deviceId){
        if(!deviceService.isExists(deviceId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        deviceService.delete(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
