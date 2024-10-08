package com.homeautomation.homeAutomation.services.impl;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    final private GroupRepository groupRepository;
    final private DeviceRepository deviceRepository;

    public GroupServiceImpl(GroupRepository groupRepository, DeviceRepository deviceRepository) {
        this.groupRepository = groupRepository;
        this.deviceRepository = deviceRepository;
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
    public List<GroupEntity> findByUserEntity_UserId(Long userId) {
        return groupRepository.findByUserEntity_UserId(userId);
    }

    @Override
    public List<DeviceEntity> getDevices(Long groupId) {
        return deviceRepository.findByGroupEntity_GroupId(groupId);
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
