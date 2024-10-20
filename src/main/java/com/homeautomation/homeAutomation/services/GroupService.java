package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.dto.GroupDto;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    GroupEntity save(GroupEntity groupEntity);

    Iterable<GroupEntity> findAll();

    Optional<GroupEntity> findById(Long id);

    List<GroupEntity> findGroupsByUserId(Long userId);

//    List<DeviceEntity> getDevices(Long groupId);

    boolean isExists(Long id);

    void delete(Long id);

    GroupEntity partialUpdate(Long id, GroupEntity groupEntity);

    GroupEntity saveUpdate(Long id, GroupEntity groupEntity);

    boolean isOwner(Long groupId, String currentUsername);
    boolean isOwner(Long groupId, Long ruleId, String currentUsername);

    List<GroupDto> getGroupsByUser(String currentUsername);
}
