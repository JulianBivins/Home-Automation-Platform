package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
//import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.mapper.impl.DeviceMapperImpl;
//import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceMapperImpl deviceMapper;
//    @Autowired
//    private BehaviourService behaviourService;
    @Autowired
    private HomeAutomationRuleService homeAutomationRuleService;

    @GetMapping("/devices")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long deviceId) {
        Optional<DeviceEntity> returnedDeviceDto = deviceService.findOne(deviceId);
         return returnedDeviceDto.map(deviceEntity -> {
            DeviceDto deviceDto = deviceMapper.mapTo(deviceEntity);
            return new ResponseEntity<>(deviceDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/behaviours/{deviceId}/behaviours")
    //TODO need to think if a Requestbody of DeviceDto or if a long id is better
    public ResponseEntity<ArrayList<String>> retrieveBehavioursByDeviceId (@RequestBody DeviceDto deviceDto) {
//        List<BehaviourEntity> behaviours = behaviourService.getBehavioursByDeviceID(deviceDto);
        ArrayList<String> behaviourArray = new ArrayList<>();
//        for(var behaviour : behaviours) {
//            behaviourArray.add(String.valueOf(behaviour.getBehaviour()));
//        }
        return new ResponseEntity<>(behaviourArray, HttpStatus.OK);
    }

    @PostMapping("/devices/{deviceType}")
    public ResponseEntity<DeviceDto> createDevice(@PathVariable String deviceTypeString,
                                                  @RequestBody DeviceDto deviceDto) {
        DeviceDto.DeviceType deviceType = switch(deviceTypeString.toUpperCase()) {
            case "LIGHTS" -> DeviceDto.DeviceType.LIGHTS;
            case "UTILITY" -> DeviceDto.DeviceType.UTILITY;
            case "SPEAKER" -> DeviceDto.DeviceType.SPEAKER;
            case "TELEVISION" -> DeviceDto.DeviceType.TELEVISION;
            case "CAMERA" -> DeviceDto.DeviceType.CAMERA;
            default -> throw new IllegalArgumentException("Invalid deviceType: " + deviceTypeString);
        };
        deviceDto.setType(deviceType);
        DeviceEntity deviceEntity = deviceMapper.mapFrom(deviceDto);
        DeviceEntity savedDeviceEntity = deviceService.save(deviceEntity);
        return new ResponseEntity<>(deviceMapper.mapTo(savedDeviceEntity), HttpStatus.CREATED);
    }

    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> updateFullDevice(@PathVariable Long deviceId, @RequestBody DeviceDto deviceDto) {
        if(!deviceService.isExists(deviceId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        deviceDto.setDeviceId(deviceId);
        DeviceEntity updatedDeviceEntity = deviceMapper.mapFrom(deviceDto);
        DeviceEntity savedUpdateDevice = deviceService.saveUpdate(deviceId, updatedDeviceEntity);
        return new ResponseEntity<>(deviceMapper.mapTo(savedUpdateDevice), HttpStatus.OK);
    }


    @PatchMapping("/devices/{deviceId}")
    public ResponseEntity<DeviceDto> partialUpdate(@PathVariable Long id, @RequestBody DeviceDto deviceDto) {
        if(!deviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DeviceEntity deviceEntity = deviceMapper.mapFrom(deviceDto);
        DeviceEntity updatedDevice = deviceService.partialUpdate(id, deviceEntity);
        return new ResponseEntity<>(
                deviceMapper.mapTo(updatedDevice),
                HttpStatus.OK);
    }

//    @DeleteMapping("/devices/rules/{deviceId}")
//    public ResponseEntity deleteDeviceOutOfRule(@PathVariable Long deviceId) {
//         if(homeAutomationRuleService.isDeviceExistsInRule(deviceId)) {
//             homeAutomationRuleService.removeDeviceFromRule(deviceId);
//             return new ResponseEntity(HttpStatus.NO_CONTENT);
//         }else {
//             return new ResponseEntity(HttpStatus.NOT_FOUND);
//         }
//    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity deleteDevice (@PathVariable Long deviceId){
        deviceService.delete(deviceId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
