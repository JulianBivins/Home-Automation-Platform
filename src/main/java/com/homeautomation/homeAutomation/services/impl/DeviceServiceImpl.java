package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.GroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    final private DeviceRepository deviceRepository;

//    final private GroupRepository groupRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository
//                             ,GroupRepository groupRepository
    ) {
        this.deviceRepository = deviceRepository;
//        this.groupRepository = groupRepository;
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
    public Optional<DeviceEntity> findOne(Long id) {
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
        return deviceRepository.findByRules_GroupEntities_GroupId(groupId);

    }
}
