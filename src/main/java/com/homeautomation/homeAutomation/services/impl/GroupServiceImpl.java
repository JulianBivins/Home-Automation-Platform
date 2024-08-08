package com.homeautomation.homeAutomation.services.impl;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.GroupService;

import java.util.List;
import java.util.Optional;

public class GroupServiceImpl implements GroupService {

    final private GroupRepository groupRepository;
    final private DeviceService deviceService;

    public GroupServiceImpl(GroupRepository groupRepository, DeviceService deviceService) {
        this.groupRepository = groupRepository;
        this.deviceService = deviceService;
    }

    @Override
    public GroupEntity save(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    @Override
    public Iterable<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<GroupEntity> findOne(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<DeviceEntity> getDevices(Long groupId) {
        return deviceService.getDevicesByGroupId(groupId);
    }

    @Override
    public boolean isExists(Long id) {
        return groupRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
