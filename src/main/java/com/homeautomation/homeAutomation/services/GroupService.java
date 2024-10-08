package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    GroupEntity save(GroupEntity groupEntity);

    Iterable<GroupEntity> findAll();

    Optional<GroupEntity> findOne(Long id);

    List<GroupEntity> findByUserEntity_UserId(Long userId);

    List<DeviceEntity> getDevices(Long groupId);

    boolean isExists(Long id);

    void delete(Long id);
}
