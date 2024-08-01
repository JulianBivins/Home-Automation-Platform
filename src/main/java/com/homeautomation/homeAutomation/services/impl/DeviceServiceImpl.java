package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    final private DeviceRepository deviceRepository;

    final private GroupService groupService;

    public DeviceServiceImpl(DeviceRepository deviceRepository, GroupService groupService) {
        this.deviceRepository = deviceRepository;
        this.groupService = groupService;
    }

    @Override
    public DeviceEntity saveUpdate(
            Long id,
            DeviceEntity deviceEntity) {
        deviceEntity.setDevice_Id(id);
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

    //TODO
    @Override
    public List<DeviceEntity> getDeviceByGroupId(Long groupId) {
        return groupService.getDevices(groupId);
    }

    @Override
    public DeviceEntity partialUpdate(Long id, DeviceEntity deviceEntity) {
        deviceEntity.setDevice_Id(id);
        return deviceRepository.findById(id).map(existingDevice -> {
            Optional.ofNullable(deviceEntity.getName()).ifPresent(existingDevice::setName);
            Optional.ofNullable(deviceEntity.getType()).ifPresent(existingDevice::setType);
            Optional.ofNullable(deviceEntity.getGroupEntity()).ifPresent(existingDevice::setGroupEntity);
            Optional.ofNullable(deviceEntity.getBehaviourEntities()).ifPresent(existingDevice::setBehaviourEntities);
            return deviceRepository.save(existingDevice);
        }).orElseThrow(() -> new RuntimeException("Device does not exist"));
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }
}
