package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.mapper.impl.DeviceMapperImpl;
import com.homeautomation.homeAutomation.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceMapperImpl deviceMapper;

    @GetMapping("/devices")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long deviceId) {
        Optional<DeviceEntity> returnedDeviceDto = deviceService.findOne(deviceId);
         return returnedDeviceDto.map(deviceEntity -> {
            DeviceDto deviceDto = deviceMapper.mapTo(deviceEntity);
            return new ResponseEntity<>(deviceDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/devices/{deviceType}")
    public ResponseEntity<DeviceDto> createDevice(@PathVariable DeviceEntity.DeviceType deviceType,
                                                  @RequestBody DeviceDto deviceDto) {
        //Type of device needs to be set manually
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
        deviceDto.setDevice_Id(deviceId);
        DeviceEntity updatedDeviceEntity = deviceMapper.mapFrom(deviceDto);
        DeviceEntity savedUpdateDevice = deviceService.saveUpdate(deviceId, updatedDeviceEntity);
        return new ResponseEntity<>(deviceMapper.mapTo(savedUpdateDevice), HttpStatus.OK);
    }


    @PatchMapping("/users/{userId}")
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

    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        //right now deleted with or without actually checking if device exists in the homeAutomationRuleDB exists.
        //should change implementation to include return type of boolean for self measures
        //also this needs to delete the device out of the HomeAutomationDB not out of RegisteredDeviceDB itself
            // user can still have to lights but have exchanged the lightbulb from the livingroom into the kithcen hence
            // also changing the applied rule set
        deviceService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
