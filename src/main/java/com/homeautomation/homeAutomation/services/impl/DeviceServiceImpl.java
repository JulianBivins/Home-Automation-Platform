package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service("deviceService")//SpEL expressions
public class DeviceServiceImpl implements DeviceService {

    final private DeviceRepository deviceRepository;
    final private UserService userService;
    final Mapper<DeviceEntity, DeviceDto> deviceMapper;

//    final private GroupRepository groupRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, UserService userService, Mapper<DeviceEntity, DeviceDto> deviceMapper
//                             ,GroupRepository groupRepository
    ) {
        this.deviceRepository = deviceRepository;
//        this.groupRepository = groupRepository;
        this.userService = userService;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public DeviceEntity saveUpdate(
            Long id,
            DeviceEntity deviceEntity) {
        deviceEntity.setDeviceId(id);
        return deviceRepository.save(deviceEntity);
    }

    @Override
    public DeviceEntity save(DeviceEntity deviceEntity) {
        return deviceRepository.save(deviceEntity);
    }

    @Override
    public Iterable<DeviceEntity> findAll() {
        return deviceRepository.findAll();
    }


    @Override
    public Optional<DeviceEntity> findById(Long id) {
        return deviceRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return deviceRepository.existsById(id);
    }

//    //TODO
//    @Override
//    public List<DeviceEntity> getDevicesByGroupId(Long groupId) {
//        return deviceRepository.findByGroupEntity_GroupId(groupId);
//    }

    @Override
    public DeviceEntity partialUpdate(Long id, DeviceEntity deviceEntity) {
        deviceEntity.setDeviceId(id);
        return deviceRepository.findById(id).map(existingDevice -> {
            Optional.ofNullable(deviceEntity.getName()).ifPresent(existingDevice::setName);
            Optional.ofNullable(deviceEntity.getType()).ifPresent(existingDevice::setType);
            Optional.ofNullable(deviceEntity.getRules()).ifPresent(newRules -> {
                existingDevice.setRules(new ArrayList<>(newRules));
            });
            return deviceRepository.save(existingDevice);
        }).orElseThrow(() -> new RuntimeException("Device does not exist"));
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }

    public List<DeviceEntity> findDevicesByUserId(Long userId) {
        return deviceRepository.findByUserEntity_UserId(userId);
    }

    public List<DeviceEntity> findDevicesByRuleId(Long ruleId) {
        return deviceRepository.findByRules_RuleId(ruleId);
    }

    public List<DeviceEntity> findDevicesByGroupId(Long groupId) {
//        return deviceRepository.findByRules_GroupEntities_GroupId(groupId);
        return deviceRepository.findByRules_GroupEntity_GroupId(groupId);
    }

    @Override
    public boolean isOwner(Long deviceId, String currentUsername) {
        Optional<DeviceEntity> device = deviceRepository.findById(deviceId);
        return device.isPresent() && device.get().getUserEntity().getUsername().equals(currentUsername);
    }

    @Override
    public List<DeviceDto> getDevicesByUser(String currentUsername) {
        UserEntity userEntity = userService.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("There exists no such user"));
        Long userId = userEntity.getUserId();
        List<DeviceEntity> devicesByUserId = findDevicesByUserId(userId);
        return devicesByUserId.stream().map(deviceMapper::mapTo).toList();
    }
}
